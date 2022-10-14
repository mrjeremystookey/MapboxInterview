package dev.bigfootprint.mapboxinterview

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.mapbox.common.TileStoreOptions.MAPBOX_ACCESS_TOKEN
import com.mapbox.geojson.Point
import com.mapbox.maps.*
import com.mapbox.maps.plugin.Plugin
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager

class MainActivity : ComponentActivity() {

    private lateinit var myGlobalMap: MapboxMap
    private lateinit var myAnnotationManager: PointAnnotationManager


    override fun onCreate(savedInstanceState: Bundle?) {

        val initialCameraOptions = CameraOptions.Builder()
            .center(Point.fromLngLat(-74.0066, 40.7135))
            .pitch(45.0)
            .zoom(15.5)
            .bearing(-17.6)
            .build()
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                Column(
                    Modifier
                        .background(Color.DarkGray)
                        .padding(10.dp),
                ) {
                    Button(modifier = Modifier.fillMaxWidth(), onClick = { zoomToFairmount() }) {
                        Text(getString(R.string.zoom_to_fairmount))
                    }
                    Divider(
                        modifier = Modifier.padding(5.dp),
                        thickness = 2.dp,
                        color = Color.Cyan
                    )
                    AndroidView(
                        modifier = Modifier
                            .fillMaxSize(),
                        factory = { context ->
                            val mapInitOptions = MapInitOptions(
                                resourceOptions = ResourceOptions.Builder()
                                    .accessToken(BuildConfig.MAPBOX_ACCESS_TOKEN)
                                    .build(),
                                context = context
                            )
                            MapView(context = context, mapInitOptions = mapInitOptions)
                        }

                    ) { mapView ->
                        val myAnnotationApi = mapView.annotations
                        val newYorkCityMarker: PointAnnotationOptions =
                            PointAnnotationOptions()
                                .withPoint(Point.fromLngLat(-74.0066, 40.7135))
                                .withIconImage(BitmapFactory.decodeResource(resources, R.drawable.red_marker))
                        myGlobalMap = mapView.getMapboxMap()
                        myGlobalMap.setCamera(initialCameraOptions)
                        myGlobalMap.loadStyleUri(Style.MAPBOX_STREETS)
                        myAnnotationManager = myAnnotationApi.createPointAnnotationManager()
                        myAnnotationManager.create(newYorkCityMarker)
                    }
                }
            }
        }
    }

    private fun zoomToFairmount() {
        myGlobalMap.flyTo(
            cameraOptions = CameraOptions.Builder()
                .center(Point.fromLngLat(-85.6505, 40.4153))
                .pitch(75.0)
                .zoom(14.5)
                .bearing(-20.6)
                .build()
        )
        val fairmountMarker: PointAnnotationOptions =
            PointAnnotationOptions()
                .withPoint(Point.fromLngLat(-85.6525, 40.4120))
                .withIconImage(BitmapFactory.decodeResource(resources, R.drawable.red_marker))
        myAnnotationManager.create(fairmountMarker)
    }
}





