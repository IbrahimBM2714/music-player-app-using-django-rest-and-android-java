from django.urls import path, include
from musicApi.views import MusicViewSet
from rest_framework import routers

router = routers.DefaultRouter()
router.register(r'music', MusicViewSet)

urlpatterns = [
    path('api/music/', MusicListCreateAPIView.as_view(), name='music-list'),
    path('api/music/<int:pk>/', MusicFileServeAPIView.as_view(), name='music')
]
