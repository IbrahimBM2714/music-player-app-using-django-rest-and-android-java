from django.http import HttpResponse, Http404
from rest_framework import viewsets
from wsgiref.util import FileWrapper
from musicApi.models import Music
from musicApi.serializers import MusicSerialzer


class MusicViewSet(viewsets.ModelViewSet):
    queryset = Music.objects.all()
    serializer_class = MusicSerialzer

    def retrieve(self, request, *args, **kwargs):
        instance = self.get_object()
        file_path = instance.mp3.path

        try:
            file = open(file_path, 'rb')
            response = HttpResponse(FileWrapper(
                file), content_type='audio/mpeg')
            
            response['Content-Disposition'] = f'attachment; filename="{instance.title}.mp3"'
            return response
        except IOError:
            raise Http404
