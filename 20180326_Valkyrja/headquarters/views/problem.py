from django.core.exceptions import ObjectDoesNotExist
from django.http import HttpResponse

from headquarters import bugle
from headquarters.error_code import ErrorCode
from headquarters.models import User, Exam, Problem, AnswerSheet, Course, TestData, LoggingInUser

import json


def problems_list(request, c_id, e_id):
    response_data = {"errorCode": ErrorCode.OK}

    try:
        # exam = Exam.objects.get(id=e_id)
        course = Course.objects.get(id=c_id)
    except ObjectDoesNotExist:
        response_data["errorCode"] = ErrorCode.ExamNotFound
    else:
        problems = []

        for exam in course.exams.all():
            if exam.id == int(e_id):
                # original code start here
                for problem in exam.problems.all():
                    problems.append({
                        "problemId": problem.id,
                        "problemName": problem.title,
                        "description": problem.description
                    })
            # else:
            #     problems.append("no")

        response_data["content"] = {
            "problems": problems
        }

    return HttpResponse(json.dumps(response_data))


def create_problem(request, c_id, e_id):
    response_data = {"errorCode": ErrorCode.OK}

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        exam = Exam.objects.get(id=e_id)

        title = req_body["title"]
        description = req_body["description"]

        test_data_arr = req_body["testdata"]
    except KeyError:
        response_data["errorCode"] = ErrorCode.TooFewArgument
    except ObjectDoesNotExist:
        response_data["errorCode"] = ErrorCode.ExamNotFound
    else:
        problem = Problem.objects.create(
            title=title,
            description=description
        )

        for td in test_data_arr:
            test_data = TestData.objects.create(
                input=td["input"],
                output=td["output"]
            )

            problem.test_data.add(test_data)

        problem.save()

        exam.problems.add(problem)
        exam.save()

        response_data["content"] = {
            "problemId": problem.id
        }

    return HttpResponse(json.dumps(response_data))


def source_code_and_result(request, c_id, e_id, p_id, s_id):
    response_data = {"errorCode": ErrorCode.OK}

    if request.method == "GET":
        try:
            student = User.objects.get(id=s_id)
        except ObjectDoesNotExist:
            response_data["errorCode"] = ErrorCode.StudentNotFound
        else:
            ans_sheet = AnswerSheet.objects.filter(student=student)

            if not ans_sheet.exists():
                response_data["errorCode"] = ErrorCode.AnswerSheetNotFound
            else:
                ans_sheet = ans_sheet.first()

                response_data["content"] = {
                    "sourceCode": ans_sheet.source_code,
                    "fileName": ans_sheet.source_code_file_name,
                    "result": ans_sheet.student_result
                }
    elif request.method == "POST":
        try:
            req_body = json.loads(request.body.decode("utf-8"))

            student = User.objects.get(id=s_id)
            problem = Problem.objects.get(id=p_id)
            course = Course.objects.get(id=c_id)
            exam = Exam.objects.get(id=e_id)

            record = LoggingInUser.objects.get(user__id=s_id)

            code = req_body["sourceCode"]
            file_name = req_body["sourceCodeFileName"]
            # test_result = req_body["testResult"]
        except KeyError:
            response_data["errorCode"] = ErrorCode.TooFewArgument
        except ObjectDoesNotExist:
            response_data["errorCode"] = ErrorCode.SomethingNotFound
        else:
            AnswerSheet.objects.create(
                source_code=code,
                source_code_file_name=file_name,
                problem=problem,
                student=student,
                # student_result=test_result
            )

            bugle.student_submit(teacher=course.teacher,
                                 student=student,
                                 exam=exam,
                                 problem=problem)

    return HttpResponse(json.dumps(response_data))


def problem_scoring_and_comment(request, c_id, e_id, p_id, s_id):
    response_data = {"errorCode": ErrorCode.OK}

    if request.method == "GET":
        try:
            student = User.objects.get(student_id=s_id)
        except ObjectDoesNotExist:
            response_data["errorCode"] = ErrorCode.StudentNotFound
        else:
            ans_sheet = AnswerSheet.objects.filter(student=student)

            if not ans_sheet.exists():
                response_data["errorCode"] = ErrorCode.AnswerSheetNotFound
            else:
                ans_sheet = ans_sheet.first()

                response_data["content"] = {
                    "score": ans_sheet.score,
                    "comment": ans_sheet.comment
                }
    elif request.method == "POST":
        try:
            req_body = json.loads(request.body.decode("utf-8"))

            student = User.objects.get(id=s_id)
            problem = Problem.objects.get(id=p_id)
            exam = Exam.objects.get(id=e_id)

            score = req_body["score"]
            comment = req_body["comment"]

            ans_sheet = AnswerSheet.objects.filter(student=student, problem=problem)
        except ObjectDoesNotExist:
            response_data["errorCode"] = ErrorCode.SomethingNotFound
        else:
            if not exam.finish_scoring:
                if ans_sheet.exist():
                    ans_sheet.score = score
                    ans_sheet.comment = comment
                    ans_sheet.save()
                else:
                    AnswerSheet.objects.create(
                        score=score,
                        comment=comment,
                        problem=problem,
                        student=student
                    )
            else:
                response_data["errorCode"] = ErrorCode.ExamAlreadyFinishScoring

    return HttpResponse(json.dumps(response_data))


def get_test_data(request, c_id, e_id, p_id):
    response_data = {"errorCode": ErrorCode.OK}

    try:
        problem = Problem.objects.get(id=p_id)
    except ObjectDoesNotExist:
        response_data["errorCode"] = ErrorCode.ProblemNotFound
    else:
        test_data = []

        for td in problem.test_data.all():
            test_data.append({
                "input": td.input,
                "output": td.output
            })

        response_data["content"] = {
            "testdata": test_data
        }

    return HttpResponse(json.dumps(response_data))
