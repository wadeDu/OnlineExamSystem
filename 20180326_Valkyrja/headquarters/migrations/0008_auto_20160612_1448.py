# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('headquarters', '0007_auto_20160612_1445'),
    ]

    operations = [
        migrations.AlterField(
            model_name='snapshot',
            name='student',
            field=models.ForeignKey(to='headquarters.User'),
        ),
    ]
