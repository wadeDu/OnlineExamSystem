# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('headquarters', '0006_auto_20160521_0738'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='snapshot',
            name='thumbnail',
        ),
        migrations.AlterField(
            model_name='snapshot',
            name='student',
            field=models.ForeignKey(to='headquarters.User', blank=True),
        ),
    ]
