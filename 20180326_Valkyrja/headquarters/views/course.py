from django.core.exceptions import ObjectDoesNotExist
from django.http import HttpResponse

from headquarters.error_code import ErrorCode
from headquarters.models import User, LoggingInUser, Course
from headquarters.user_state import UserState

import json


def courses_list(request):
    response_data = {"errorCode": ErrorCode.OK}

    courses = Course.objects.all()

    response_data["content"] = {
        "courses": [{
            "courseId": course.id,
            "courseName": course.title,
            "year": course.year,
            "semester": course.semester,
            "teacherName": course.teacher.name
        } for course in courses]
    }

    return HttpResponse(json.dumps(response_data))


def create_course(request):
    response_data = {"errorCode": ErrorCode.OK}

    try:
        req_body = json.loads(request.body.decode("utf-8"))

        teacher_id = req_body["userId"]
        course_name = req_body["courseName"]
        year = req_body["year"]
        semester = req_body["semester"]
        student_ids = req_body["studentIds"]

        course = Course.objects.create(
            title=course_name,
            year=year,
            semester=semester,
            teacher=User.objects.get(id=teacher_id)
        )

        for student_id in student_ids:
            course.students.add(User.objects.get(student_id=student_id))

        response_data["content"] = {
            "courseId": course.id
        }
    except KeyError:
        response_data["errorCode"] = ErrorCode.TooFewArgument
    except ObjectDoesNotExist:
        response_data["errorCode"] = ErrorCode.StudentNotFound

    return HttpResponse(json.dumps(response_data))


def remove_course(request, c_id):
    response_data = {"errorCode": ErrorCode.OK}

    try:
        course = Course.objects.get(id=c_id)
    except ObjectDoesNotExist:
        response_data["errorCode"] = ErrorCode.CourseNotFound
    else:
        course.delete()

    return HttpResponse(json.dumps(response_data))


def course_students_list(request, c_id):
    response_data = {"errorCode": ErrorCode.OK}

    try:
        course = Course.objects.get(id=c_id)
    except ObjectDoesNotExist:
        response_data["errorCode"] = ErrorCode.CourseNotFound
    else:
        students = []

        for student in course.students.all():
            record = LoggingInUser.objects.filter(user=student)
            is_login = record.exists()

            students.append({
                "userId":student.id,
                "studentId": student.student_id,
                "name": student.name,
                "graduateYear": student.graduate_year,
                "state": UserState(record.first().state).name if is_login else "logout",
                # "profilePhoto": student.profile_photo.decode("utf-8"),
                "admin": student.is_admin,
            })

        response_data["content"] = {
            "students": students
        }

    return HttpResponse(json.dumps(response_data))
