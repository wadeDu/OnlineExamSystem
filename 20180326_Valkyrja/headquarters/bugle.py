from headquarters.models import LoggingInUser, Course, User, Exam, Problem, Snapshot

import socket
import json


# call() should be renamed to dispatchMessages()
def call(targets_ip: list, message: dict):
    print("bugle.call--->")
    for target in targets_ip:
        print("bugle.call: %s !" % target)
        sock = socket.socket()
        sock.connect((target, 3001))

        sock.sendall(json.dumps(message).encode('UTF-8'))

        sock.close()


def new_message(exam: Exam, user: User, message: str):
    course = Course.objects.get(exams__id=exam.id)

    teacher = course.teacher
    # students = course.students.all()
    students = course.students.all().exclude(student_id=user.student_id)
    #for s in students:
    #    print(s)
    records = LoggingInUser.objects.filter(user=students) | LoggingInUser.objects.filter(user=teacher)

    print("!!bugle.new_msg-rec#: %d" % len(records))
    if len(records) > 0:
        call([record.ip_address for record in records], {
            "type": "Chat",
            "action": "NewMessage",
            "content": {
                "name": user.name,
                "message": message,
            }
        })


def student_login(student: User):
    teachers = LoggingInUser.objects.filter(user__is_admin=True)

    print("student_login-teacher#: " + str(len(teachers)))
    if len(teachers) > 0:
        call([teacher.ip_address for teacher in teachers], {
            "type": "User",
            "action": "Login",
            "content": {
                "name": student.name
            }
        })


def student_logout(student: User):
    teachers = LoggingInUser.objects.filter(user__is_admin=True)
    print("student_logout-teachers#: %d" % len(teachers))
    if len(teachers) > 0:
        call([teacher.ip_address for teacher in teachers], {
            "type": "User",
            "action": "Logout",
            "content": {
                "name": student.name
            }
        })


def student_idle(student: User):
    teachers = LoggingInUser.objects.filter(user__is_admin=True)

    call([teacher.ip_address for teacher in teachers], {
        "type": "User",
        "action": "Idle",
        "content": {
            "name": student.name
        }
    })


def student_submit(teacher: User, student: User, exam: Exam, problem: Problem):
    call(LoggingInUser.objects.get(user=teacher).ip_address, {
        "type": "Exam",
        "action": "Submit",
        "content": {
            "name": student.name,
            "examName": exam.name,
            "problemName": problem.name
        }
    })


# Yao
def student_send_snapshot(student: User, snapshot: Snapshot, term_of_pic: int):
    #print("@bugle => term_of_pic: ",term_of_pic)
    teachers = LoggingInUser.objects.filter(user__is_admin=True)
    if len(teachers) > 0:
        call([teacher.ip_address for teacher in teachers], {
            "type": "Monitor",
            "action": "SendSnapshot",
            "content": {
                "id": student.id,
                "name": student.name,
                "studentId": student.student_id,
                "snapshot": snapshot.snapshot.decode("utf-8"),
                "term_of_pic": term_of_pic,
                "time": str(snapshot.create_time),
            }
        })


def request_new_snapshot(student: User):
    call(LoggingInUser.objects.get(user=student).ip_address, {
        "type": "Exam",
        "action": "RequestSnapshot"
    })

def request_textRec(student: User):
    #print("@bugle.py => request_textRec: ",student)
    call([LoggingInUser.objects.get(user=student).ip_address], {
        "type": "Monitor",
        "action": "RequestTextRec"
    })

# Yao 修改增加[]，call ([ ... ])

def send_key_event(teacher: User, key_event: dict):
    #print("@bugle.py => send_key_event: ")
    call([LoggingInUser.objects.get(user=teacher).ip_address], {
        "type": "Monitor",
        "action": "KeyEvent",
        "content":
            key_event
    })

def send_key_text(teacher: User, student_name: str, keyText: str):
    #print("@bugle.py => send_key_text: ")
    call([LoggingInUser.objects.get(user=teacher).ip_address], {
        "type": "Monitor",
        "action": "KeyText",
        "content":{
            "name": student_name,
            "keyText": keyText,
        }
    })

#20171128
def send_text_rec(teacher: User, student_name: str, textRec: str):
    #print("@bugle.py => send_text_rec: ", textRec)
    call([LoggingInUser.objects.get(user=teacher).ip_address], {
        "type": "Monitor",
        "action": "SendTextRec",
        "content":{
            "name": student_name,
            "textRec": textRec,
        }
    })

#20170918 catch pram from exam.py's start_monitor;
def start_monitor(student: User, proctor_id:int, proctor:str, scale: float, freq: int):
#def start_monitor(student: User, proctor_id:int, proctor:str):

    #print("@bugle.py start_monitor ,scale=",scale,",freq=",freq )
    #print("proctor_id=",proctor_id,"proctor=",proctor,"student=",student.name)
    #print(type(student.name),type(proctor))
    call([LoggingInUser.objects.get(user=student).ip_address], {
        "type": "Monitor",
        "action": "Start",
        "content":{
            "name": student.name,
            #"student_name": student.name,
            #"proctor_id":proctor_id,
            #"proctor": proctor,
            "scale": scale,
            "freq": freq,
        }
    })


def stop_monitor(student: User):
    #print("@bugle.py => stop_monitor: ",student)
    call([LoggingInUser.objects.get(user=student).ip_address], {
        "type": "Monitor",
        "action": "Stop"
    })


def pause_exam(exam: Exam):
    course_students = Course.objects.get(exams__id=exam.id).students.all()
    online_students = LoggingInUser.objects.filter(user__in=course_students)

    call([student.ip_address for student in online_students], {
        "type": "Exam",
        "action": "Pause"
    })


def resume_exam(exam: Exam):
    course_students = Course.objects.get(exams__id=exam.id).students.all()
    online_students = LoggingInUser.objects.filter(user__in=course_students)

    call([student.ip_address for student in online_students], {
        "type": "Exam",
        "action": "Resume"
    })


def halt_exam(exam: Exam):
    course_students = Course.objects.get(exams__id=exam.id).students.all()
    online_students = LoggingInUser.objects.filter(user__in=course_students)

    call([student.ip_address for student in online_students], {
        "type": "Exam",
        "action": "Halt"
    })


def extend_exam(exam: Exam, extend_time: int):
    course_students = Course.objects.get(exams__id=exam.id).students.all()
    online_students = LoggingInUser.objects.filter(user__in=course_students)

    call([student.ip_address for student in online_students], {
        "type": "Exam",
        "action": "Extend",
        "content": {
            "extend_time": extend_time
        }
    })


# Yao
def attend_exam(student: LoggingInUser, s_id:int):
    teachers = LoggingInUser.objects.filter(user__is_admin=True)

    call([teacher.ip_address for teacher in teachers], {
        "type": "Exam",
        "action": "Attend",
        "content": {
            "id": s_id,
            "name": str(student.user),
            "state": student.state
        }
    })

