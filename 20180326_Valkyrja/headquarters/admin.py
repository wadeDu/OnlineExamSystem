from django.contrib import admin

from headquarters.models import User, Course, Exam, Problem, TestData, ChatMessage, Snapshot, \
    AnswerSheet, ExamResult, LoggingInUser  # ,ImageAdmin

# Register your models here.
admin.site.register(User)
admin.site.register(Course)
admin.site.register(Exam)
admin.site.register(Problem)
admin.site.register(TestData)
admin.site.register(ChatMessage)
admin.site.register(Snapshot)  # ,ImageAdmin)
admin.site.register(AnswerSheet)
admin.site.register(ExamResult)
admin.site.register(LoggingInUser)