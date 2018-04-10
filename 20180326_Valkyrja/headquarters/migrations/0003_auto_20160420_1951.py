# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('headquarters', '0002_auto_20151223_1423'),
    ]

    operations = [
        migrations.AlterField(
            model_name='answersheet',
            name='score',
            field=models.IntegerField(default=0),
        ),
    ]
