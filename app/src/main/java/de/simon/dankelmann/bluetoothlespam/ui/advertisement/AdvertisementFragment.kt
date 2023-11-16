package de.simon.dankelmann.bluetoothlespam.ui.advertisement

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable.INFINITE
import com.airbnb.lottie.LottieDrawable.RepeatMode
import de.simon.dankelmann.bluetoothlespam.AppContext.AppContext
import de.simon.dankelmann.bluetoothlespam.Enums.AdvertisementError
import de.simon.dankelmann.bluetoothlespam.Enums.AdvertisementSetRange
import de.simon.dankelmann.bluetoothlespam.Enums.AdvertisementSetType
import de.simon.dankelmann.bluetoothlespam.Enums.AdvertisementTarget
import de.simon.dankelmann.bluetoothlespam.Interfaces.Callbacks.IAdvertisementServiceCallback
import de.simon.dankelmann.bluetoothlespam.Models.AdvertisementSet
import de.simon.dankelmann.bluetoothlespam.Models.AdvertisementSetCollection
import de.simon.dankelmann.bluetoothlespam.R
import de.simon.dankelmann.bluetoothlespam.databinding.FragmentAdvertisementBinding
import kotlin.reflect.typeOf


class AdvertisementFragment : Fragment(), IAdvertisementServiceCallback {

    private val _logTag = "AdvertisementFragment"
    private var _viewModel: AdvertisementViewModel? = null
    private var _binding: FragmentAdvertisementBinding? = null
    private var _advertisementSetCollection: AdvertisementSetCollection? = null

    companion object {
        fun newInstance() = AdvertisementFragment()
    }

    private lateinit var viewModel: AdvertisementViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewModel = ViewModelProvider(this)[AdvertisementViewModel::class.java]
        _viewModel = viewModel
        _binding = FragmentAdvertisementBinding.inflate(inflater, container, false)
        val root: View = _binding!!.root

        // Get AdvertisementSetCollection from Bundle
        if(arguments != null){
            var advertisementSetCollectionArgumentKey = "advertisementSetCollection"

            var advertismentSetCollection = AdvertisementSetCollection()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val type: Class<AdvertisementSetCollection> = AdvertisementSetCollection::class.java
                var collectionFromBundle = requireArguments().getSerializable(advertisementSetCollectionArgumentKey, type)
                if(collectionFromBundle != null){
                    advertismentSetCollection = collectionFromBundle
                }
            } else {
                var collectionFromBundle = requireArguments().getSerializable(advertisementSetCollectionArgumentKey)
                if(collectionFromBundle != null){
                    advertismentSetCollection = collectionFromBundle as AdvertisementSetCollection
                }
            }

           setAdvertisementSetCollection(advertismentSetCollection)
        }

        setupUi()

        return root//inflater.inflate(R.layout.fragment_advertisement, container, false)
    }

    override fun onResume() {
        super.onResume()
        AppContext.getAdvertisementSetQueueHandler().addAdvertisementServiceCallback(this)
    }

    override fun onPause() {
        super.onPause()
        AppContext.getAdvertisementSetQueueHandler().removeAdvertisementServiceCallback(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AdvertisementViewModel::class.java)
        // TODO: Use the ViewModel
    }

    fun onPlayButtonClicked(){
        if(_viewModel!!.isAdvertising.value!!){
            AppContext.getAdvertisementSetQueueHandler().deactivate()
            _viewModel!!.isAdvertising.postValue(false)
        } else {
            AppContext.getAdvertisementSetQueueHandler().activate()
            _viewModel!!.isAdvertising.postValue(true)
        }
    }

    fun setAdvertisementSetCollection(advertisementSetCollection: AdvertisementSetCollection){
        _advertisementSetCollection = advertisementSetCollection

        _viewModel!!.advertisementSetCollectionTitle.postValue(advertisementSetCollection.title)
        _viewModel!!.advertisementSetCollectionSubTitle.postValue(getAdvertisementSetCollectionSubTitle(advertisementSetCollection))

        // Pass the Collection to the Queue Handler
        AppContext.getAdvertisementSetQueueHandler().setAdvertisementSetCollection(advertisementSetCollection)
    }

    fun getAdvertisementSetCollectionSubTitle(advertisementSetCollection: AdvertisementSetCollection):String{
        var subtitle = "${advertisementSetCollection.getTotalNumberOfAdvertisementSets()} Sets in ${advertisementSetCollection.getNumberOfLists()} Lists"
        return subtitle
    }

    fun getAdvertisementSetSubtitle(advertisementSet: AdvertisementSet):String{

        var type = when(advertisementSet.type){
            AdvertisementSetType.ADVERTISEMENT_TYPE_UNDEFINED -> "Undefined"
            AdvertisementSetType.ADVERTISEMENT_TYPE_SWIFT_PAIRING -> "Swift Pairing"
            AdvertisementSetType.ADVERTISEMENT_TYPE_FAST_PAIRING -> "Fast Pairing"
            AdvertisementSetType.ADVERTISEMENT_TYPE_CONTINUITY_DEVICE_POPUPS -> "iOs Device Popup"
            AdvertisementSetType.ADVERTISEMENT_TYPE_CONTINUITY_ACTION_MODALS -> "iOs Action Modal"
        }

        var range = when(advertisementSet.range){
            AdvertisementSetRange.ADVERTISEMENTSET_RANGE_CLOSE -> "Close"
            AdvertisementSetRange.ADVERTISEMENTSET_RANGE_MEDIUM -> "Medium"
            AdvertisementSetRange.ADVERTISEMENTSET_RANGE_FAR -> "Far"
            AdvertisementSetRange.ADVERTISEMENTSET_RANGE_UNKNOWN -> "Unknown"
        }

        return "Type: $type, Range: $range"
    }

    fun setupUi(){

        // Views
        var playButton = _binding!!.advertisementFragmentPlayButton
        var advertisingAnimation = _binding!!.advertisementFragmentAdvertisingAnimation
        var advertisingTargetImage = _binding!!.advertisementFragmentTargetImage
        var advertisementSetCollectionTitle = _binding!!.advertisementFragmentCollectionTitle
        var advertisementSetCollectionSubTitle = _binding!!.advertisementFragmentCollectionSubtitle
        var advertisementSetTitle = _binding!!.advertisementFragmentCurrentSetTitle
        var advertisementSetSubTitle = _binding!!.advertisementFragmentCurrentSetSubTitle

        // Listeners
        playButton.setOnClickListener{
            onPlayButtonClicked()
        }

        // Observers
        _viewModel!!.isAdvertising.observe(viewLifecycleOwner) { isAdvertising ->
            if(isAdvertising){
                playButton.setImageDrawable(resources.getDrawable(R.drawable.pause, AppContext.getContext().theme))
                advertisingAnimation.playAnimation()
            } else {
                playButton.setImageDrawable(resources.getDrawable(R.drawable.play_arrow, AppContext.getContext().theme))
                advertisingAnimation.cancelAnimation()
                advertisingAnimation.frame = 0
            }
        }

        _viewModel!!.target.observe(viewLifecycleOwner) { target ->
            var targetImageDrawable:Drawable = when(target){
                AdvertisementTarget.ADVERTISEMENT_TARGET_UNDEFINED -> resources.getDrawable(R.drawable.bluetooth, AppContext.getContext().theme)
                AdvertisementTarget.ADVERTISEMENT_TARGET_IOS -> resources.getDrawable(R.drawable.apple, AppContext.getContext().theme)
                AdvertisementTarget.ADVERTISEMENT_TARGET_ANDROID -> resources.getDrawable(R.drawable.ic_android, AppContext.getContext().theme)
                AdvertisementTarget.ADVERTISEMENT_TARGET_WINDOWS -> resources.getDrawable(R.drawable.microsoft, AppContext.getContext().theme)
            }
            advertisingTargetImage.setImageDrawable(targetImageDrawable)
        }

        _viewModel!!.advertisementSetCollectionTitle.observe(viewLifecycleOwner) { value ->
            advertisementSetCollectionTitle.text = value
        }

        _viewModel!!.advertisementSetCollectionSubTitle.observe(viewLifecycleOwner) { value ->
            advertisementSetCollectionSubTitle.text = value
        }

        _viewModel!!.advertisementSetTitle.observe(viewLifecycleOwner) { value ->
            advertisementSetTitle.text = value
        }

        _viewModel!!.advertisementSetSubTitle.observe(viewLifecycleOwner) { value ->
            advertisementSetSubTitle.text = value
        }

    }

    // AdvertismentServiceCallback
    override fun onAdvertisementSetStart(advertisementSet: AdvertisementSet?) {
        Log.d(_logTag, "onAdvertisementSetStart")
    }

    override fun onAdvertisementSetStop(advertisementSet: AdvertisementSet?) {
        Log.d(_logTag, "onAdvertisementSetStop")
    }

    override fun onAdvertisementSetSucceeded(advertisementSet: AdvertisementSet?) {
        if(advertisementSet != null){
            _viewModel!!.target.postValue(advertisementSet.target)
            _viewModel!!.advertisementSetTitle.postValue(advertisementSet.title)
            _viewModel!!.advertisementSetSubTitle.postValue(getAdvertisementSetSubtitle(advertisementSet))
        }
    }

    override fun onAdvertisementSetFailed(advertisementSet: AdvertisementSet?, advertisementError: AdvertisementError) {
        Log.d(_logTag, "onAdvertisementSetFailed")
    }
    // END: AdvertismentServiceCallback
}