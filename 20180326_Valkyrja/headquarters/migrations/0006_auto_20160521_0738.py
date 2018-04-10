# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('headquarters', '0005_auto_20160521_0737'),
    ]

    operations = [
        migrations.AlterField(
            model_name='snapshot',
            name='thumbnail',
            field=models.BinaryField(blank=True),
        ),
    ]
