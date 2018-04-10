# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('headquarters', '0003_auto_20160420_1951'),
    ]

    operations = [
        migrations.AddField(
            model_name='snapshot',
            name='image',
            field=models.ImageField(upload_to='', blank=True),
        ),
    ]
