package ac.id.pnj.broadbandmultimedia.bm2015

import ac.id.pnj.broadbandmultimedia.bm2015.database.SavedResult
import ac.id.pnj.broadbandmultimedia.bm2015.database.SavedResultAdapter
import ac.id.pnj.broadbandmultimedia.bm2015.database.database
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import org.jetbrains.anko.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class SavedActivity : AppCompatActivity() {
    private var savedResult: MutableList<SavedResult> = mutableListOf()
    private lateinit var adapter: SavedResultAdapter
    private lateinit var listResult: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_saved)
        linearLayout {
            lparams (width = matchParent, height = wrapContent)
            topPadding = dip(16)
            leftPadding = dip(16)
            rightPadding = dip(16)

            swipeRefresh = swipeRefreshLayout {
                setColorSchemeResources(
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light)

                listResult = recyclerView {
                    lparams (width = matchParent, height = wrapContent)
                    layoutManager = LinearLayoutManager(context)
                }
            }
        }

        adapter = SavedResultAdapter(savedResult){
            val sendData = Intent(applicationContext,ResultActivity::class.java)
            sendData.putExtra("randomIdIntent", it.randomId)
            sendData.putExtra("networkConfigurationIntent",it.configGpon)
            sendData.putExtra("powerTransmitterIntent",it.powerTransmitter)
            sendData.putExtra("panjangFiberIntent",it.fiberlength)
            sendData.putExtra("totalSplicesIntent",it.numberOfSplicing)
            sendData.putExtra("totalConnectorIntent",it.numberOfConnector)
            sendData.putExtra("photoUriIntent",it.photoUri)
            startActivity(sendData)
        }
        listResult.adapter = adapter
        showSavedResult()
        swipeRefresh.onRefresh {
            showSavedResult()
        }
    }

    private fun showSavedResult(){
        savedResult.clear()
        database.use {
            swipeRefresh.isRefreshing = false
            val result = select(SavedResult.TABLE_SAVED)
            val saveResult = result.parseList(classParser<SavedResult>())
            savedResult.addAll(saveResult)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        showSavedResult()
    }
}


