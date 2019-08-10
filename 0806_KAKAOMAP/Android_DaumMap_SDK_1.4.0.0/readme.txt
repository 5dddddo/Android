* Daum Map Android library
http://apis.map.kakao.com/android/

* Kakao Developer Forum
https://devtalk.kakao.com

* Technical Support
kris.shin@kakaocorp.com


change log -----------------------------------------------
- 1.4.0.0 (2019/07/22)
  * apache http library 사용 제거
  * arm64-v8a 지원 추가

- 1.3.1.0 (2017/08/01)
  * 일부 OS에서 발생하는 HTTPS 인증 오류 수정

- 1.3.0.0 (2017/05/22)
  * Kakao Developers와 통합을 위한 인증 API 수정

- 1.2.19.3 (2017/01/06)
  * Android 6.0 Runtime Permission 대응

- 1.2.19.2 (2016/11/15)
  * Null Pointer Exception 나던 부분 수정

- 1.2.19.1 (2016/09/28)
  * MapView가 초기화가 진행 중인 과정 중에 일부 API 호출 시 Crash 나던 이슈 수정

- 1.2.19.0 (2016/09/11)
  * 보안 취약성이 있던 코드 수정

- 1.2.18.2 (2016/08/22)
  * OpenAPI 인증 실패 시 Callback이 호출 되지 않던 이슈 수정

- 1.2.18.1 (2016/06/15)
  * MapView 초기화 단계에서 HD2X로 모드 변경 시 Crash 나던 이슈 수정
  * Android 6.0에서 권한 문제로 Crash 나던 이슈 수정
  * OpenAPI 인증 실패 시  처리  방식 변경

- 1.2.18.0 (2016/03/14)
  * MapTileMode Enum 추가 및 MapView에 setTileMode/getTileMode 추가하여 일반 HD 모드 외에 HD 2X 모드 추가

- 1.2.17.3 (2016/01/29)
  * MapView의 getMapPointBounds() 메서드에서 topRight 값이 올바르지 않았던 이슈 수정
  * MapPoiItem의 CallOutBalloon을 Disable 시킨 상태에서는 Marker 이미지가 Selected 상태로 나오지 않던 이슈 수정
  * 특정 케이스에서 setShowCurrentLocationMarker() 메서드가 제대로 작동하지 않던 문제 수정
  * ReverseGeoCoding에서 Device의 언어 설정이 일부 유럽 언어로 선택되어 있을 때 String.format() 메서드에서 반환하는 문자열이 잘못 되는 경우에 대한 문제 수정

- 1.2.17.2 (2015/10/19)
  * 베가레이서2(4.0.3)에서 MapView 사이즈를 변경하면서 마커를 추가하면 화면 깨지는 현상 수정
  * MapPoiItem의 setName 메서드를 호출 하였을 때 MapView에 이미 추가된 객체인 경우에도 반영이 되도록 수정

- 1.2.17.1 (2015/7/6)
  * MapPoiItem의 setMapPoint 메서드를 호출 하였을 때 MapView에 이미 추가된 객체인 경우에도 반영이 되도록 수정

- 1.2.17.0 (2015/7/6)
  * MapPoiItem에 Custom Image의 ID 외에 Bitmap을 지정할 수 있는 메서드 추가
  * MapView 클래스에 public MapPointBounds getMapPointBounds() 메서드 추가

- 1.2.16.0 (2015/6/18)
  * 메모리 누수 수정
  * 일부 static 메서드 Deprecate 하고  class 메서드로  제공  수정

- 1.2.15.5 (2015/3/26)
  * 버그 픽스

- 1.2.15 (2015/1/12)
  * CalloutBalloonAdapter 추가 - 말풍선을 필요시에만 생성해 마커 추가 속도 향상

- 1.2.14 (2014/12/9)
  * 원타일(여러 타일 레이어를 서버에서 병합한 타일) 적용

- 1.2.13 (2014/11/4)
  * 샘플앱에 프로가드 적용
  * 기본 마커 버그 수정

- 1.2.12 (2014/10/23)
  * 최초 라이브러리 로딩시 검은화면 대신 보여줄 뷰를 설정 가능하도록 MapView를 감싸는 MapLayout 추가

- 1.2.11 (2014/9/29)
  * 현위치 트래킹시 여러장의 마커 이미지를 순차적으로 보여주는 애니메이션 추가

- 1.2.9 (2014/9/12)
  * 커스텀 말풍선 Pressed 효과 추가
  * 마커 이미지 다양한 dpi 지원
  * MapView 객체를 해제시키는 API 추가
  * 탭과 같은 미세한 움직임에는 dragStarted, dragEnded 이벤트 발생하지 않도록 수정

- 1.2.8 (2014/9/4)
  * 중심점과 직경(meter)으로 카메라 움직이는 기능 추가
  * CurrentLocationEventListener, POIItemEventListener NPE 수정

- 1.2.7 (2014/8/27)
  * 현위치 트래킹시 지도가 이동하지 않는 모드 추가
  * 지도 이동시 애니메이션이 안되는 현상 수정
  * selectPOIItem() 사용시 말풍선이 보이지 않는 현상 수정

- 1.2.6 (2014/8/14)
  * 커스텀 말풍선 기능 추가
  * 좌표-주소 변환 기능 개선

- 1.2.5 (2014/7/23)
  * onMapViewDragStarted, onMapViewDragEnded, onMapViewMoveFinished 이벤트 리스너 추가
  * moveCamera(CameraUpdate cameraUpdate) 메소드 추가
  * findPOIItemByName 메소드 호출시 발생하는 ClassCastException 처리
  * 여러개의 마커가 있을때, 마커를 차례대로 누르면 활성화/비활성화 아이콘이 비정상적으로 나타나는 현상 수정
  * 데모앱을 리스트뷰로 변경

- 1.2.4 (2014/7/1)
  * 마커 선택 효과 버그 수정

- 1.2.2 (2014/6/16)
  * 커스텀 현위치 마커 이미지, 반경 선/면 컬러 적용 기능 추가
  * 마커 선택 효과 추가
  * 말풍선 좌/우측 버튼 추가 기능 추가
  * 원그리기 기능 추가

- 1.2.1 (2013/3/12)
  * bug fix : net.daum.mf.map.api.MapView.POIItemEventListener.onPOIItemSelected() is not called for POI item that is not draggable.

- 1.2.0 (2012/11/1)
  * HD Map Tile support (@see net.daum.mf.map.api.MapView.setHDMapTileEnabled(boolean))
  * Map Tile Persistent Cache support (@see net.daum.mf.map.api.MapView.setMapTilePersistentCacheEnabled(boolean))
  * armv7 binary support (libs/armeabi, libs/armeabi-v7a)
  * enhanced CalloutBalloon/Pin UI design
  * When using custom image for MapPOIItem, anchor point of custom image is set to bottom-center point by default
  * do not change zoom level when current location tracking is activated
  * bug fix for net.daum.mf.map.api.MapView.getPOIItems() and getPolylines()

- 1.0.8 (2012/8/24)
  * fix crash issue when closing map-view

- 1.0.7 (2012/5/31)
  * fix for NullPointerException case of MapView.getMapCenterPoint()

- 1.0.5 (2012/5/7)
  * improve accuracy of current location tracking functionality
  * map tile versioning

- 1.0.1 (2012/4/18)
  * net.daum.mf.map.api.MapView.MapViewEventListener.onMapViewInitialized(MapView) event added
  * support embedding net.daum.mf.map.api.MapView as custom-view component in Android layout XML

- 1.0 (2012/3/16)
  * initial release
