from rest_framework import serializers
from musicApi.models import Music


class MusicSerialzer(serializers.ModelSerializer):
    class Meta:
        model = Music
        fields = "__all__"
