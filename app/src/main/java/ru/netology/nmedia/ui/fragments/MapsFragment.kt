package ru.netology.nmedia.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.collections.MarkerManager
import com.google.maps.android.ktx.awaitAnimateCamera
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.model.cameraPosition
import com.google.maps.android.ktx.utils.collection.addMarker
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.MapBinding
import ru.netology.nmedia.ui.dto.MyMarker
import ru.netology.nmedia.ui.extensions.icon
import ru.netology.nmedia.ui.extensions.stringToLatLong
import ru.netology.nmedia.ui.repository.MarkerRepository
import ru.netology.nmedia.ui.viewmodel.MarkerViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MapsFragment() : Fragment() {
    private var _binding: MapBinding? = null
    private val binding: MapBinding
        get() = _binding!!
    private lateinit var googleMap: GoogleMap
    private val viewModel: MarkerViewModel by activityViewModels()

    @Inject
    lateinit var repository: MarkerRepository

    @SuppressLint("MissingPermission")
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                googleMap.apply {
                    isMyLocationEnabled = true
                    uiSettings.isMyLocationButtonEnabled = true
                }
            } else {
                // TODO: show sorry dialog
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.map, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = MapBinding.inflate(layoutInflater)
//        .setOnClickListener {
//            println("кнопка работает")
//            findNavController().navigate(R.id.action_mapsFragment_to_allMarkersFragment)
//        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        lifecycle.coroutineScope.launchWhenCreated {
            googleMap = mapFragment.awaitMap().apply {
                isTrafficEnabled = true
                isBuildingsEnabled = true
                uiSettings.apply {
                    isZoomControlsEnabled = true
                    setAllGesturesEnabled(true)
                }
            }
            when {
                // 1. Проверяем есть ли уже права
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    googleMap.apply {
                        isMyLocationEnabled = true
                        uiSettings.isMyLocationButtonEnabled = true
                    }

                    val fusedLocationProviderClient = LocationServices
                        .getFusedLocationProviderClient(requireActivity())

                    fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                        println(it)
                    }
                }
                // 2. Должны показать обоснование необходимости прав
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    // TODO: show rationale dialog
                }
                // 3. Запрашиваем права
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }




            val target = LatLng(55.751999, 37.617734)
            val markerManager = MarkerManager(googleMap)

            val markersList = viewModel.getMarkers()

            val collection: MarkerManager.Collection = markerManager.newCollection().apply {
                markersList.forEach { marker ->
                    addMarker {
                        tag.apply { marker.tag }
                        position(stringToLatLong(marker.position))
                        icon(getDrawable(requireContext(), R.drawable.ic_netology_48dp)!!)
                    }
                }
            }

            collection.setOnMarkerClickListener { marker ->
                val id = marker.id.substringAfter("m").toInt()
                val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                builder
                    .setMessage(R.string.markerDialog)
                    //Отмена нажатия на маркер
                    .setNeutralButton(R.string.cancel) { dialog, _ ->
                        dialog.cancel()
                    }
                    //Удаление маркера из БД и с карты
                    .setNegativeButton(R.string.delete) { dialog, _ ->
                        collection.remove(marker)
                        viewModel.removeById(id)
                        dialog.cancel()
                    }
                    //Редактирование тега маркера
                    .setPositiveButton(R.string.edit) { dialog, _ ->
                        findNavController().navigate(R.id.action_mapsFragment_to_allMarkersFragment)
                        //Навигация в редактирование маркера?
                        dialog.cancel()
                    }

                val dialog: AlertDialog = builder.create()
                dialog.show()

//                marker.tag.apply {
//                }
//                Toast.makeText(
//                    requireContext(),
//                    (marker.tag as String),
//                    Toast.LENGTH_LONG
//                ).show()
                true
            }

            googleMap.setOnMapLongClickListener { latLng ->
                collection.addMarker {
                    position(latLng)
                    icon(getDrawable(requireContext(), R.drawable.ic_netology_48dp)!!)
                }
                val newMarker = collection.markers.last()
                val myMarker = MyMarker(
                    id = newMarker.id.substringAfter("m").toInt(),
                    title = newMarker.id.toString(),
                    position = newMarker.position.toString(),
                    tag = newMarker.tag.toString(),
                    googleId = newMarker.id
                )
                viewModel.addMarker(myMarker)
            }

            googleMap.awaitAnimateCamera(
                CameraUpdateFactory.newCameraPosition(
                    cameraPosition {
                        target(target)
                        zoom(15F)
                    }
                ))
//            binding.allMarkersButton.setOnClickListener { println("Кнопка работает") }
        }

    }
}