from django.db import models

# Create your models here.


class Music(models.Model):
    title = models.CharField(max_length=100)
    artist = models.CharField(max_length=100)
    file = models.FileField(upload_to='music_files/')
