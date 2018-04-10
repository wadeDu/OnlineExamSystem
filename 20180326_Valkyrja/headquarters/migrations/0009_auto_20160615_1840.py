# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('headquarters', '0008_auto_20160612_1448'),
    ]

    operations = [
        migrations.AlterField(
            model_name='answersheet',
            name='score',
            field=models.IntegerField(),
        ),
    ]
