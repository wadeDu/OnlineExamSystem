# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('headquarters', '0004_snapshot_image'),
    ]

    operations = [
        migrations.RenameField(
            model_name='snapshot',
            old_name='image',
            new_name='thumbnail',
        ),
    ]
