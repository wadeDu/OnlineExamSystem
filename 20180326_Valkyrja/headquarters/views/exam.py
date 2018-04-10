from datetime import datetime

from django.core.exceptions import ObjectDoesNotExist, MultipleObjectsReturned
from django.http import HttpResponse
# Yao add
from django.core.serializers.json import DjangoJSONEncoder

from headquarters import bugle
from headquarters.error_code import ErrorCode
from headquarters.models import User, LoggingInUser, Course, Exam, Snapshot, ExamResult, ChatMessage
from headquarters.user_state import UserState

import json


def exams_list(request, c_id):
    response_data = {"errorCode": ErrorCode.OK}

    try:
        course = Course.objects.get(id=c_id)
    except ObjectDoesNotExist:
        response_data["errorCode"] = ErrorCode.CourseNotFound
    else:
        exams = []

        epoch = datetime.utcfromtimestamp(0)

        for exam in course.exams.all():
            exams.append({
                "examId": exam.id,
                "examName": exam.title,
                "startTime": (exam.start_time.replace(tzinfo=None) - epoch).total_seconds() * 1000,
                "duration": exam.duration
            })

        response_data["content"] = {
            "exams": exams
        }

    return HttpResponse(json.dumps(response_data))


def create_exam(request, c_id):
    response_data = {"errorCode": ErrorCode.OK, "content": {}}

    try:
        req_body = json.loads(request.body.decode("utf-8"))

        exam_title = req_body["examName"]
        start_time = req_body["beginTime"]
        duration = req_body["duration"]

        course = Course.objects.get(id=c_id)
    except KeyError:
        response_data["errorCode"] = ErrorCode.TooFewArgument
    except ObjectDoesNotExist:
        response_data["errorCode"] = ErrorCode.CourseNotFound
    else:
        exam = Exam.objects.create(
                title=exam_title,
                start_time=start_time,
                duration=duration
        )

        course.exams.add(exam)

        response_data["content"] = {
            "examId": exam.id
        }

    return HttpResponse(json.dumps(response_data))


def remove_exam(request, c_id, e_id):
    response_data = {"errorCode": ErrorCode.OK}

    try:
        exam = Exam.objects.get(id=e_id)
    except ObjectDoesNotExist:
        response_data["errorCode"] = ErrorCode.ExamNotFound
    else:
        exam.delete()

    return HttpResponse(json.dumps(response_data))


def attend_exam(request, c_id, e_id, s_id):
    response_data = {"errorCode": ErrorCode.OK}

    try:
        # (user__student_id=s_id)
        record = LoggingInUser.objects.get(user__id=s_id)
    except ObjectDoesNotExist:
        response_data["errorCode"] = ErrorCode.UserNotLoggedIn
    except MultipleObjectsReturned:
        response_data["errorCode"] = ErrorCode.MultipleObjectsReturned
    else:
        record.state = UserState.InExam.value
        record.save()
        bugle.attend_exam(record, s_id)

    return HttpResponse(json.dumps(response_data))


def snapshot(request, c_id, e_id, s_id):

    response_data = {"errorCode": ErrorCode.OK}

    if request.method == "GET":
        # user__student_id=s_id
        # Exception Type: FieldError
        # Exception Value: Cannot resolve keyword 「user」 into field;
        #                  Choices are: create_time, exam, exam_id, id, snapshot, student, student_id
        # models.py Snapshot, Foreign Key
        snapshot_objs = Snapshot.objects.filter(exam_id=e_id, student_id=s_id)

        snapshots = []

        for obj in snapshot_objs:
            snapshots.append({
                "snapshot": obj.snapshot.decode("utf-8"),
                "time": obj.create_time
            })
        response_data["content"] = {
            "snapshots": snapshots
        }
    elif request.method == "POST":
        try:
            student = User.objects.get(id=s_id)
            req_body = json.loads(request.body.decode("utf-8"))

            exam = Exam.objects.get(id=e_id)
            image = req_body["snapshot"]
            term_of_pic = req_body["term_of_pic"]

        except KeyError:
            response_data["errorCode"] = ErrorCode.TooFewArgument
        except ObjectDoesNotExist:
            response_data["errorCode"] = ErrorCode.ExamNotFound
        else:
            snapshot_obj = Snapshot.objects.create(
                    snapshot=bytes(image, "utf-8"),
                    exam=exam,
                    student=User.objects.get(id=s_id)
            )
            bugle.student_send_snapshot(student, snapshot_obj, term_of_pic)

    # Yao add [cls=DjangoJSONEncoder]
    return HttpResponse(json.dumps(response_data, cls=DjangoJSONEncoder))


def request_snapshot(request, c_id, e_id, s_id):
    response_data = {"errorCode": ErrorCode.OK}

    try:
        student = User.objects.get(student_id=s_id)
    except ObjectDoesNotExist:
        response_data["errorCode"] = ErrorCode.StudentNotFound
    else:
        bugle.request_new_snapshot(student)

    return HttpResponse(json.dumps(response_data))


# 20170915 add snapshot parameters from teacher
def start_monitor(request, c_id, e_id, s_id):
    response_data = {"errorCode": ErrorCode.OK}
    #print("@exam.py start_monitor from teacher with param")

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        # student_id=s_id
        student = User.objects.get(id=s_id)

        # 20170918 catch proctor!!
        proctor_id = req_body["proctorId"]
        scale = req_body["scale"]
        freq = req_body["freq"]
        proctor = User.objects.get(id=proctor_id)
        #print("@exam.py proctor_id=", proctor_id, ",proctor=", proctor, ",scale=", scale, "and freq=", freq, "student=",
        #      student)
        # print("@exam.py proctor_id=",proctor_id,",proctor=",proctor)

    except ObjectDoesNotExist:
        response_data["errorCode"] = ErrorCode.StudentNotFound
    else:

        bugle.start_monitor(student, proctor_id, proctor, scale, freq)

    return HttpResponse(json.dumps(response_data))


def stop_monitor(request, c_id, e_id, s_id):
    response_data = {"errorCode": ErrorCode.OK}

    try:
        student = User.objects.get(id=s_id)
    except ObjectDoesNotExist:
        response_data["errorCode"] = ErrorCode.StudentNotFound
    else:
        bugle.stop_monitor(student)

    return HttpResponse(json.dumps(response_data))


def request_textRec(request, c_id, e_id, s_id):
    response_data = {"errorCode": ErrorCode.OK}

    try:
        student = User.objects.get(id=s_id)
        #print("@exam.py => request_textRec: ",student)

    except ObjectDoesNotExist:
        response_data["errorCode"] = ErrorCode.StudentNotFound
    else:
        bugle.request_textRec(student)

    return HttpResponse(json.dumps(response_data))


def handle_key_event(request, c_id, e_id, s_id):
    response_data = {"errorCode": ErrorCode.OK}

    try:
        req_body = json.loads(request.body.decode("utf-8"))

        key_event_type = req_body["keyEventType"]
        key_code = req_body["keyCode"]
        key_char = req_body["keyChar"]

        course = Course.objects.get(id=c_id)

        student = User.objects.get(student_id=s_id)
        #print("@exam.py => handle_key_event: ", student, student.name, student.student_id)

    except KeyError:
        response_data["errorCode"] = ErrorCode.TooFewArgument
    except ObjectDoesNotExist:
        response_data["errorCode"] = ErrorCode.ExamNotFound
    else:
        bugle.send_key_event(course.teacher, {
            "name": student.name,
            "keyEventType": key_event_type,
            "keyCode": key_code,
            "keyChar": key_char
        })

    return HttpResponse(json.dumps(response_data))

def handle_key_text(request, c_id, e_id, s_id):
    response_data = {"errorCode": ErrorCode.OK}

    try:
        req_body = json.loads(request.body.decode("utf-8"))

        keyText = req_body["keyText"]

        course = Course.objects.get(id=c_id)

        student = User.objects.get(student_id=s_id)
        #print("@exam.py => handle_key_text: ", student, student.name, student.student_id)

    except KeyError:
        response_data["errorCode"] = ErrorCode.TooFewArgument
    except ObjectDoesNotExist:
        response_data["errorCode"] = ErrorCode.ExamNotFound
    else:
        bugle.send_key_text(course.teacher, student.name, keyText)

    return HttpResponse(json.dumps(response_data))

#20171128
def handle_text_rec(request, c_id, e_id, s_id):
    response_data = {"errorCode": ErrorCode.OK}

    try:
        req_body = json.loads(request.body.decode("utf-8"))

        textRec = req_body["textRec"]

        course = Course.objects.get(id=c_id)

        student = User.objects.get(student_id=s_id)
        #print("@exam.py => handle_text_rec: ", student, student.student_id, textRec)

    except KeyError:
        response_data["errorCode"] = ErrorCode.TooFewArgument
    except ObjectDoesNotExist:
        response_data["errorCode"] = ErrorCode.ExamNotFound
    else:
        bugle.send_text_rec(course.teacher, student.name, textRec)

    return HttpResponse(json.dumps(response_data))



def exam_scoring_and_comment(request, c_id, e_id, s_id):
    response_data = {"errorCode": ErrorCode.OK}

    if request.method == "GET":
        try:
            exam = Exam.objects.get(id=e_id)
            student = User.objects.get(student_id=s_id)

            result = ExamResult.objects.get(exam=exam, student=student)
        except ObjectDoesNotExist:
            response_data["errorCode"] = ErrorCode.SomethingNotFound
        else:
            response_data["score"] = result.score
            response_data["comment"] = result.comment
    elif request.method == "POST":
        try:
            req_body = json.loads(request.body.decode("utf-8"))

            score = req_body["score"]
            comment = req_body["comment"]

            exam = Exam.objects.get(id=e_id)
            student = User.objects.get(student_id=s_id)
        except KeyError:
            response_data["errorCode"] = ErrorCode.TooFewArgument
        except ObjectDoesNotExist:
            response_data["errorCode"] = ErrorCode.SomethingNotFound
        else:
            if not exam.finish_scoring:
                ExamResult.objects.filter(exam=exam, student=student).delete()

                ExamResult.objects.create(
                        exam=exam,
                        student=student,
                        score=score,
                        comment=comment
                )
            else:
                response_data["errorCode"] = ErrorCode.ExamAlreadyFinishScoring

    return HttpResponse(json.dumps(response_data))


def finish_scoring(request, c_id, e_id):
    response_data = {"errorCode": ErrorCode.OK}

    try:
        exam = Exam.objects.get(id=e_id)
    except ObjectDoesNotExist:
        response_data["errorCode"] = ErrorCode.ExamNotFound
    else:
        exam.finish_scoring = True
        exam.save()

    return HttpResponse(json.dumps(response_data))


def exam_result(request, c_id, e_id):
    response_data = {"errorCode": ErrorCode.OK, "content": {}}

    try:
        exam = Exam.objects.get(id=e_id)
        course = Course.objects.get(id=c_id)
    except ObjectDoesNotExist:
        response_data["errorCode"] = ErrorCode.SomethingNotFound
    else:
        exam_results = ExamResult.objects.filter(exam=exam)

        students = []
        student_ids = []

        for result in exam_results:
            student = exam_results.student

            students.append({
                "id": student.student_id,
                "name": student.name,
                "score": result.score,
                "comment": result.comment
            })

            student_ids.append(student.student_id)

        missing_students = [(student.name, student.student_id) for student in course.students.all()
                            if student.student_id not in student_ids]

        for student_id, name in missing_students:
            students.append({
                "id": student_id,
                "name": name,
                "score": 0,
                "comment": None
            })

        response_data["content"] = {
            "students": students
        }

    return HttpResponse(json.dumps(response_data))


def pause_exam(request, c_id, e_id):
    response_data = {"errorCode": ErrorCode.OK}

    try:
        exam = Exam.objects.get(id=e_id)
    except ObjectDoesNotExist:
        response_data["errorCode"] = ErrorCode.ExamNotFound
    else:
        bugle.pause_exam(exam=exam)

    return HttpResponse(json.dumps(response_data))


def resume_exam(request, c_id, e_id):
    response_data = {"errorCode": ErrorCode.OK}

    try:
        exam = Exam.objects.get(id=e_id)
    except ObjectDoesNotExist:
        response_data["errorCode"] = ErrorCode.ExamNotFound
    else:
        bugle.resume_exam(exam=exam)

    return HttpResponse(json.dumps(response_data))


def halt_exam(request, c_id, e_id):
    response_data = {"errorCode": ErrorCode.OK}

    try:
        exam = Exam.objects.get(id=e_id)
    except ObjectDoesNotExist:
        response_data["errorCode"] = ErrorCode.ExamNotFound
    else:
        bugle.halt_exam(exam=exam)

    return HttpResponse(json.dumps(response_data))


def extend_exam(request, c_id, e_id):
    response_data = {"errorCode": ErrorCode.OK}

    try:
        req_body = json.loads(request.body.decode("utf-8"))

        exam = Exam.objects.get(id=e_id)
        extend_time = int(req_body["extendMinutes"])
    except KeyError:
        response_data["errorCode"] = ErrorCode.TooFewArgument
    except ValueError:
        response_data["errorCode"] = ErrorCode.WrongArgumentType
    except ObjectDoesNotExist:
        response_data["errorCode"] = ErrorCode.ExamNotFound
    else:
        bugle.extend_exam(exam=exam, extend_time=extend_time)

    return HttpResponse(json.dumps(response_data))


def send_message(request, c_id, e_id):
    response_data = {"errorCode": ErrorCode.OK}
    print("in send_message!!!")

    try:
        req_body = json.loads(request.body.decode("utf-8"))

        user_id = req_body["userId"]
        message = req_body["message"]

        user = User.objects.get(id=user_id)
        exam = Exam.objects.get(id=e_id)

        print("@exam.py user_id=", user_id, "user=", user)
    except KeyError:
        response_data["errorCode"] = ErrorCode.TooFewArgument
    except ValueError:
        response_data["errorCode"] = ErrorCode.WrongArgumentType
    except ObjectDoesNotExist:
        response_data["errorCode"] = ErrorCode.SomethingNotFound
    else:
        ChatMessage.objects.create(
                message=message,
                exam=exam,
                user=user
        )

        bugle.new_message(exam=exam, user=user, message=message)

    return HttpResponse(json.dumps(response_data))


def chat_history(request, c_id, e_id):
    response_data = {"errorCode": ErrorCode.OK, "content": {}}

    try:
        exam = Exam.objects.get(id=e_id)
    except ObjectDoesNotExist:
        response_data["errorCode"] = ErrorCode.ExamNotFound
    else:
        messages = []

        epoch = datetime.utcfromtimestamp(0)

        for chat_message in ChatMessage.objects.filter(exam=exam):
            messages.append({
                "message": chat_message.message,
                "from": chat_message.user.name,
                "role": chat_message.user.role,
                "time": (chat_message.create_time.replace(tzinfo=None) - epoch).total_seconds() * 1000
            })

        response_data["content"] = {
            "messages": messages
        }

    return HttpResponse(json.dumps(response_data))
