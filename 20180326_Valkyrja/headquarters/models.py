from django.db import models
from django.utils import timezone


class User(models.Model):
    username = models.CharField(max_length=20)
    password = models.CharField(max_length=64)
    name = models.CharField(max_length=20)
    student_id = models.CharField(max_length=20, unique=True)
    is_admin = models.BooleanField()
    graduate_year = models.IntegerField()
    email = models.EmailField()
    profile_photo = models.BinaryField()

    create_time = models.DateTimeField(editable=False)
    modify_time = models.DateTimeField()

    def save(self, *args, **kwargs):
        if not self.id:
            self.create_time = timezone.now()
        self.modify_time = timezone.now()
        return super().save(*args, **kwargs)

    @property
    def role(self):
        return "teacher" if self.is_admin else "student"

    def __str__(self):
        return self.name


class Course(models.Model):
    title = models.CharField(max_length=100)
    year = models.IntegerField()
    semester = models.IntegerField()
    teacher = models.ForeignKey("User", related_name="teacher")
    students = models.ManyToManyField("User", related_name="students")
    exams = models.ManyToManyField("Exam")

    def __str__(self):
        return self.title


class Exam(models.Model):
    title = models.CharField(max_length=100)
    start_time = models.DateTimeField()
    duration = models.IntegerField()
    finish_scoring = models.BooleanField()
    problems = models.ManyToManyField("Problem")

    create_time = models.DateTimeField(editable=False)
    modify_time = models.DateTimeField()

    def save(self, *args, **kwargs):
        if not self.id:
            self.finish_scoring = False
            self.create_time = timezone.now()

        self.modify_time = timezone.now()
        return super().save(*args, **kwargs)

    def __str__(self):
        return self.title


class Problem(models.Model):
    title = models.CharField(max_length=100)
    description = models.CharField(max_length=1000)
    test_data = models.ManyToManyField("TestData")

    def __str__(self):
        return self.title


class TestData(models.Model):
    input = models.CharField(max_length=5000)
    output = models.CharField(max_length=5000)


class ChatMessage(models.Model):
    message = models.CharField(max_length=1000)
    exam = models.ForeignKey("Exam")
    user = models.ForeignKey("User")

    create_time = models.DateTimeField(editable=False)

    def save(self, *args, **kwargs):
        if not self.id:
            self.create_time = timezone.now()

        return super().save(*args, **kwargs)

    def __str__(self):
        return str(self.user) + " _ " + self.message


class Snapshot(models.Model):
    snapshot = models.BinaryField()
    exam = models.ForeignKey("Exam")
    student = models.ForeignKey("User")

    create_time = models.DateTimeField(editable=False)

    def save(self, *args, **kwargs):
        if not self.id:
            self.create_time = timezone.now()
        return super().save(*args, **kwargs)

    def __str__(self):
        return str(self.student.name) + " _ " + str(self.create_time)


class AnswerSheet(models.Model):
    source_code = models.CharField(max_length=3000)
    source_code_file_name = models.CharField(max_length=30)
    student_result = models.CharField(max_length=5000)
    score = models.IntegerField()
    comment = models.CharField(max_length=1000)
    problem = models.ForeignKey("Problem")
    student = models.ForeignKey("User")

    create_time = models.DateTimeField(editable=False)

    def save(self, *args, **kwargs):
        if not self.id:
            self.create_time = timezone.now()

        return super().save(*args, **kwargs)

    def __str__(self):
        return self.student


class ExamResult(models.Model):
    score = models.IntegerField()
    comment = models.CharField(max_length=1000)
    exam = models.ForeignKey("Exam")
    student = models.ForeignKey("User")

    create_time = models.DateTimeField(editable=False)

    def save(self, *args, **kwargs):
        if not self.id:
            self.create_time = timezone.now()

        return super().save(*args, **kwargs)

    def __str__(self):
        return self.student


class LoggingInUser(models.Model):
    ip_address = models.CharField(max_length=20)
    state = models.IntegerField()
    user = models.ForeignKey("User")
    login_time = models.DateTimeField(editable=False)

    def save(self, *args, **kwargs):
        if not self.id:
            self.login_time = timezone.now()

        return super().save(*args, **kwargs)

    def __str__(self):
        return str(self.user)
