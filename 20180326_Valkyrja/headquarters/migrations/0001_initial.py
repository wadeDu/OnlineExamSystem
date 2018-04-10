# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='AnswerSheet',
            fields=[
                ('id', models.AutoField(primary_key=True, serialize=False, verbose_name='ID', auto_created=True)),
                ('source_code', models.CharField(max_length=3000)),
                ('source_code_file_name', models.CharField(max_length=30)),
                ('student_result', models.CharField(max_length=5000)),
                ('score', models.IntegerField()),
                ('comment', models.CharField(max_length=1000)),
                ('create_time', models.DateTimeField(editable=False)),
            ],
        ),
        migrations.CreateModel(
            name='ChatMessage',
            fields=[
                ('id', models.AutoField(primary_key=True, serialize=False, verbose_name='ID', auto_created=True)),
                ('message', models.CharField(max_length=1000)),
                ('create_time', models.DateTimeField(editable=False)),
            ],
        ),
        migrations.CreateModel(
            name='Course',
            fields=[
                ('id', models.AutoField(primary_key=True, serialize=False, verbose_name='ID', auto_created=True)),
                ('title', models.CharField(max_length=100)),
                ('year', models.IntegerField()),
                ('semester', models.IntegerField()),
            ],
        ),
        migrations.CreateModel(
            name='Exam',
            fields=[
                ('id', models.AutoField(primary_key=True, serialize=False, verbose_name='ID', auto_created=True)),
                ('title', models.CharField(max_length=100)),
                ('start_time', models.DateTimeField()),
                ('duration', models.IntegerField()),
                ('finish_scoring', models.BooleanField()),
                ('create_time', models.DateTimeField(editable=False)),
                ('modify_time', models.DateTimeField()),
            ],
        ),
        migrations.CreateModel(
            name='ExamResult',
            fields=[
                ('id', models.AutoField(primary_key=True, serialize=False, verbose_name='ID', auto_created=True)),
                ('score', models.IntegerField()),
                ('comment', models.CharField(max_length=1000)),
                ('create_time', models.DateTimeField(editable=False)),
                ('exam', models.ForeignKey(to='headquarters.Exam')),
            ],
        ),
        migrations.CreateModel(
            name='LoggingInUser',
            fields=[
                ('id', models.AutoField(primary_key=True, serialize=False, verbose_name='ID', auto_created=True)),
                ('ip_address', models.CharField(max_length=20)),
                ('state', models.IntegerField()),
                ('login_time', models.DateTimeField(editable=False)),
            ],
        ),
        migrations.CreateModel(
            name='Problem',
            fields=[
                ('id', models.AutoField(primary_key=True, serialize=False, verbose_name='ID', auto_created=True)),
                ('title', models.CharField(max_length=100)),
                ('description', models.CharField(max_length=1000)),
            ],
        ),
        migrations.CreateModel(
            name='Snapshot',
            fields=[
                ('id', models.AutoField(primary_key=True, serialize=False, verbose_name='ID', auto_created=True)),
                ('snapshot', models.BinaryField()),
                ('create_time', models.DateTimeField(editable=False)),
                ('exam', models.ForeignKey(to='headquarters.Exam')),
                # ('photo', models.ImageField()),
                # ('time_interval', models.IntegerField()),
            ],
        ),
        migrations.CreateModel(
            name='TestData',
            fields=[
                ('id', models.AutoField(primary_key=True, serialize=False, verbose_name='ID', auto_created=True)),
                ('input', models.CharField(max_length=5000)),
                ('output', models.CharField(max_length=5000)),
            ],
        ),
        migrations.CreateModel(
            name='User',
            fields=[
                ('id', models.AutoField(primary_key=True, serialize=False, verbose_name='ID', auto_created=True)),
                ('username', models.CharField(max_length=20)),
                ('password', models.CharField(max_length=64)),
                ('name', models.CharField(max_length=20)),
                ('student_id', models.CharField(max_length=20)),
                ('is_admin', models.BooleanField()),
                ('graduate_year', models.IntegerField()),
                ('email', models.EmailField(max_length=254)),
                ('profile_photo', models.BinaryField()),
                ('create_time', models.DateTimeField(editable=False)),
                ('modify_time', models.DateTimeField()),
            ],
        ),
        migrations.AddField(
            model_name='snapshot',
            name='student',
            field=models.ForeignKey(to='headquarters.User'),
        ),
        migrations.AddField(
            model_name='problem',
            name='test_data',
            field=models.ManyToManyField(to='headquarters.TestData'),
        ),
        migrations.AddField(
            model_name='logginginuser',
            name='user',
            field=models.ForeignKey(to='headquarters.User'),
        ),
        migrations.AddField(
            model_name='examresult',
            name='student',
            field=models.ForeignKey(to='headquarters.User'),
        ),
        migrations.AddField(
            model_name='exam',
            name='problems',
            field=models.ManyToManyField(to='headquarters.Problem'),
        ),
        migrations.AddField(
            model_name='course',
            name='exams',
            field=models.ManyToManyField(to='headquarters.Exam'),
        ),
        migrations.AddField(
            model_name='course',
            name='students',
            field=models.ManyToManyField(related_name='students', to='headquarters.User'),
        ),
        migrations.AddField(
            model_name='course',
            name='teacher',
            field=models.ForeignKey(related_name='teacher', to='headquarters.User'),
        ),
        migrations.AddField(
            model_name='chatmessage',
            name='exam',
            field=models.ForeignKey(to='headquarters.Exam'),
        ),
        migrations.AddField(
            model_name='chatmessage',
            name='user',
            field=models.ForeignKey(to='headquarters.User'),
        ),
        migrations.AddField(
            model_name='answersheet',
            name='problem',
            field=models.ForeignKey(to='headquarters.Problem'),
        ),
        migrations.AddField(
            model_name='answersheet',
            name='student',
            field=models.ForeignKey(to='headquarters.User'),
        ),
    ]
