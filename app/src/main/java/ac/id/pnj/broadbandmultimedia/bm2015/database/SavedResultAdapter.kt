package ac.id.pnj.broadbandmultimedia.bm2015.database

import ac.id.pnj.broadbandmultimedia.bm2015.R
import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import org.jetbrains.anko.*

class SavedResultAdapter(private val resultSaved: List<SavedResult>, private val listener:(SavedResult) -> Unit)
    :RecyclerView.Adapter<SavedResultViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedResultViewHolder {
        return SavedResultViewHolder(
            ItemUI().createView(
                AnkoContext.create(parent.context, parent)
            )
        )
    }

    override fun getItemCount(): Int = resultSaved.size

    override fun onBindViewHolder(holder: SavedResultViewHolder, position: Int) {
        holder.bindItem(resultSaved[position], listener)
    }

}

class ItemUI :AnkoComponent<ViewGroup>{
    override fun createView(ui: AnkoContext<ViewGroup>): View {
        return with(ui){
            linearLayout {
                orientation = LinearLayout.HORIZONTAL
                padding = dip(16)
                backgroundResource = R.drawable.custom_border_for_image
                imageView {
                    backgroundResource = R.drawable.custom_border_transparan
                    id = R.id.photo_thumbnail
                    padding = dip(8)
                }.lparams(width = dip(100), height = dip(150) )
                linearLayout {
                    orientation = LinearLayout.VERTICAL
                    textView {
                        id = R.id.thumbnail_config
                        textColor = Color.WHITE
                        textSize = 16f //sp
                        setTypeface(typeface, Typeface.BOLD)
                        backgroundResource = R.drawable.custom_border_transparan
                    }.lparams(width = matchParent)
                    textView {
                        id = R.id.thumbnail_power
                        backgroundResource = R.drawable.custom_border_transparan
                        textColor = Color.WHITE
                        textSize = 16f //sp
                        setTypeface(typeface, Typeface.BOLD)
                    }.lparams(width = matchParent) {
                        topMargin = dip(8)
                    }
                    textView {
                        id = R.id.thumbnail_panjang
                        backgroundResource = R.drawable.custom_border_transparan
                        textColor = Color.WHITE
                        textSize = 16f //sp
                        setTypeface(typeface, Typeface.BOLD)
                    }.lparams(width = matchParent) {
                        topMargin = dip(8)
                    }
                    textView {
                        id = R.id.thumbnail_jumlah_connector
                        backgroundResource = R.drawable.custom_border_transparan
                        textColor = Color.WHITE
                        textSize = 16f //sp
                        setTypeface(typeface, Typeface.BOLD)
                    }.lparams(width = matchParent) {
                        topMargin = dip(8)
                    }
                    textView {
                        id = R.id.thumbnail_jumlah_splicing
                        backgroundResource = R.drawable.custom_border_transparan
                        textColor = Color.WHITE
                        textSize = 16f //sp
                        setTypeface(typeface, Typeface.BOLD)
                    }.lparams(width = matchParent) {
                        topMargin = dip(8)
                    }
                }.lparams(width = matchParent) {
                    topMargin = dip(8)
                    marginStart = dip(8)
                    bottomMargin = dip(16)
                }
            }
        }
    }
}

class SavedResultViewHolder(view: View) : RecyclerView.ViewHolder(view){
    private val gponConfigSaved:TextView = view.find(R.id.thumbnail_config)
    private val powerTransmitterSaved: TextView = view.find(R.id.thumbnail_power)
    private val panjangfiberSaved:TextView = view.find(R.id.thumbnail_panjang)
    private val totalConnectorSaved: TextView = view.find(R.id.thumbnail_jumlah_connector)
    private val totalSplicesSaved:TextView = view.find(R.id.thumbnail_jumlah_splicing)
    private val photoThumbnail:ImageView = view.find(R.id.photo_thumbnail)

    fun bindItem(savedResult: SavedResult, listener: (SavedResult) -> Unit){
        Glide.with(itemView.context).load(savedResult.photoUri).placeholder(R.drawable.no_image_icon).into(photoThumbnail)
        powerTransmitterSaved.text = itemView.resources.getString(R.string.input_power_transmitter_dbm) + savedResult.powerTransmitter
        gponConfigSaved.text = itemView.resources.getString(R.string.gpon_configuration)+savedResult.configGpon
        panjangfiberSaved.text = itemView.resources.getString(R.string.input_fiber_length)+savedResult.fiberlength
        totalConnectorSaved.text = itemView.resources.getString(R.string.input_connector)+savedResult.numberOfConnector
        totalSplicesSaved.text = itemView.resources.getString(R.string.input_splicing)+savedResult.numberOfSplicing
        itemView.setOnClickListener{listener(savedResult)}
    }



}