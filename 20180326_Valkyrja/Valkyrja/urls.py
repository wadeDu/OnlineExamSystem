"""Valkyrja URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/1.8/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  url(r'^$', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  url(r'^$', Home.as_view(), name='home')
Including another URLconf
    1. Add an import:  from blog import urls as blog_urls
    2. Add a URL to urlpatterns:  url(r'^blog/', include(blog_urls))
"""
from django.conf.urls import url, include, patterns
from django.contrib import admin

from Valkyrja import settings
from headquarters.views.user import *
from headquarters.views.course import *
from headquarters.views.exam import *
from headquarters.views.problem import *

urlpatterns = [
    url(r'^admin/', include(admin.site.urls)),

    url(r'^user/login$', login),
    url(r'^user/register$', register),
    url(r'^user/forget-password$', forget_password),
    url(r'^user/(?P<u_id>\d+)/reset-password$', reset_password),
    url(r'^user/(?P<u_id>\d+)$', user_profile),
    url(r'^user/(?P<u_id>\d+)/photo$', user_profile_photo),
    url(r'^user/(?P<u_id>\d+)/logout$', logout),

    url(r'^course$', courses_list),
    url(r'^course/create$', create_course),
    url(r'^course/(?P<c_id>\d+)/remove$', remove_course),
    url(r'^course/(?P<c_id>\d+)/student$', course_students_list),

    url(r'^course/(?P<c_id>\d+)/exam$', exams_list),
    url(r'^course/(?P<c_id>\d+)/exam/add$', create_exam),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/remove$', remove_exam),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/student/(?P<s_id>\w+)/score-and-comment$', exam_scoring_and_comment),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/finish-scoring$', finish_scoring),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/result$', exam_result),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/pause$', pause_exam),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/resume$', resume_exam),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/halt$', halt_exam),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/extend$', extend_exam),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/chat/send-message$', send_message),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/chat/history$', chat_history),

    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/problem$', problems_list),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/problem/add$', create_problem),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/problem/(?P<p_id>\d+)/student/(?P<s_id>\w+)/source-code$', source_code_and_result),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/problem/(?P<p_id>\d+)/student/(?P<s_id>\w+)/score-and-comment$', problem_scoring_and_comment),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/problem/(?P<p_id>\d+)/testdata$', get_test_data),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/student/(?P<s_id>\w+)/attend$', attend_exam),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/student/(?P<s_id>\w+)/snapshot$', snapshot),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/student/(?P<s_id>\w+)/request-current-snapshot$', request_snapshot),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/student/(?P<s_id>\w+)/monitor/start$', start_monitor),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/student/(?P<s_id>\w+)/monitor/stop$', stop_monitor),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/student/(?P<s_id>\w+)/monitor/requestTextRec$', request_textRec),
    # url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/student/(?P<s_id>\w+)/attend$', attend_exam),
    # url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/problem/(?P<p_id>\w+)/snapshot$', snapshot),
    # url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/problem/(?P<p_id>\w+)/request-current-snapshot$', request_snapshot),
    # url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/problem/(?P<p_id>\d+)/monitor/start$', start_monitor),
    # url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/problem/(?P<p_id>\w+)/monitor/stop$', stop_monitor),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/student/(?P<s_id>\w+)/monitor/send-key-event$', handle_key_event),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/student/(?P<s_id>\w+)/monitor/send-key-text$', handle_key_text),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/student/(?P<s_id>\w+)/monitor/send-text-rec$', handle_text_rec),
    #url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/student/(?P<s_id>\w+)/monitor/send-text-event$', handle_text_event),
]


# if settings.DEBUG:
#     urlpatterns += patterns('', (r'^Valkyrja/(?P<path>.*)$', 'django.views.static.serve',
#                                  {'document_root': settings.MEDIA_ROOT}))
