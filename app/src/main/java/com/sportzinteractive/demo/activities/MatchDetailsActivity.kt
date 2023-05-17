package com.sportzinteractive.demo.activities

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sportzinteractive.demo.R
import com.sportzinteractive.demo.adapter.CustomAdapter
import com.sportzinteractive.demo.customviews.CustomDialog
import com.sportzinteractive.demo.databinding.ActivityMatchDetailsBinding
import com.sportzinteractive.demo.databinding.DialogPlayerDetailsBinding
import com.sportzinteractive.demo.models.Batting
import com.sportzinteractive.demo.models.Bowling
import com.sportzinteractive.demo.models.MatchDetails
import org.json.JSONArray
import org.json.JSONObject

class MatchDetailsActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: ActivityMatchDetailsBinding

    private lateinit var arrayAdapter: ArrayAdapter<String>

    private var listSpinner = mutableListOf<String>()

    var teamHomeName = ""
    var teamAwayName = ""

    private lateinit var rvHomeAdapter: CustomAdapter
    private lateinit var rvAwayAdapter: CustomAdapter

    lateinit var laoder : CustomDialog

    private var dialogBinding: DialogPlayerDetailsBinding? = null
    var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_match_details)
        binding.listener = this

        laoder= CustomDialog(this)

        getSupportActionBar()?.setTitle("Match Details");
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()?.setDisplayShowHomeEnabled(true);

        listSpinner.add("Both Teams")
        arrayAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, listSpinner)
        arrayAdapter.setDropDownViewResource(R.layout.item_dd)
        binding.spinner.adapter = arrayAdapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (binding.spinner.selectedItem.toString().equals("Both Teams")) {
                    binding.rvHome.visibility = View.VISIBLE
                    binding.rvAway.visibility = View.VISIBLE
                }
                if (binding.spinner.selectedItem.toString().equals(teamHomeName)) {
                    binding.rvHome.visibility = View.VISIBLE
                    binding.rvAway.visibility = View.GONE
                }
                if (binding.spinner.selectedItem.toString().equals(teamAwayName)) {
                    binding.rvHome.visibility = View.GONE
                    binding.rvAway.visibility = View.VISIBLE
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

        initRv()

        val url = intent.extras?.getString("URL")

        val queue: RequestQueue = Volley.newRequestQueue(applicationContext)
        laoder.show()
        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            try {
                val matchDetails = JSONObject(response.getString("Matchdetail"))

                val teamHome: String = matchDetails.getString("Team_Home")
                val teamAway: String = matchDetails.getString("Team_Away")

                val match = JSONObject(matchDetails.getString("Match"))
                val date: String = match.getString("Date")
                val time: String = match.getString("Time")
                val venue: String = matchDetails.getString("Venue")

                val teams = JSONObject(response.getString("Teams"))
                val teamHomeDtls = JSONObject(teams.getString(teamHome))
                val teamAwayDtls = JSONObject(teams.getString(teamAway))

                teamHomeName = teamHomeDtls.getString("Name_Full")
                teamAwayName = teamAwayDtls.getString("Name_Full")

                listSpinner.add(teamHomeName)
                listSpinner.add(teamAwayName)
                arrayAdapter.notifyDataSetChanged()

                binding.tvHomeName.setText(teamHomeName)
                binding.tvAwayName.setText(teamAwayName)

                if (teamHomeName.equals("New Zealand"))
                    binding.imgTeamHome.setImageResource(R.drawable.new_zealand)

                if (teamHomeName.equals("South Africa"))
                    binding.imgTeamHome.setImageResource(R.drawable.south_africa)

                if (teamAwayName.equals("India"))
                    binding.imgTeamAway.setImageResource(R.drawable.india)

                if (teamAwayName.equals("Pakistan"))
                    binding.imgTeamAway.setImageResource(R.drawable.pakistan)

                val teamHomeDtlsArr = JSONArray()
                val playersObjectHome = teamHomeDtls.getJSONObject("Players")
                val keysHome = playersObjectHome.keys()
                while (keysHome.hasNext()) {
                    val playerId = keysHome.next()
                    val playerObject = playersObjectHome.getJSONObject(playerId)
                    teamHomeDtlsArr.put(playerObject)
                }
                addDataToHomeRv(teamHomeDtlsArr)

                val teamAwayDtlsArr = JSONArray()
                val playersObjectAway = teamAwayDtls.getJSONObject("Players")
                val keysAway = playersObjectAway.keys()
                while (keysAway.hasNext()) {
                    val playerId = keysAway.next()
                    val playerObject = playersObjectAway.getJSONObject(playerId)
                    teamAwayDtlsArr.put(playerObject)
                }
                addDataToAwayRv(teamAwayDtlsArr)

                laoder.dismiss()

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }, { error ->
            Log.e("TAG", "RESPONSE IS $error")
            Toast.makeText(this@MatchDetailsActivity, "Fail to get response", Toast.LENGTH_SHORT)
                .show()
        })
        queue.add(request)

    }

    private fun showPlayerDetailsDialog(it: MatchDetails) {
        dialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.dialog_player_details,
            null,
            false
        )
        dialog?.setContentView(dialogBinding!!.root)
        dialogBinding!!.ivClose.setOnClickListener {
            dialog?.dismiss()
        }

        dialogBinding!!.tvBatStyle.setText(it.Batting.Style)
        dialogBinding!!.tvBatAverage.setText(it.Batting.Average)
        dialogBinding!!.tvBatRuns.setText(it.Batting.Runs)
        dialogBinding!!.tvBatStrikeRate.setText(it.Batting.Strikerate)

        dialogBinding!!.tvBowlStyle.setText(it.Bowling.Style)
        dialogBinding!!.tvBowlAverage.setText(it.Bowling.Average)
        dialogBinding!!.tvBowlWickets.setText(it.Bowling.Wickets)
        dialogBinding!!.tvBowlEconomyRate.setText(it.Bowling.Economyrate)

        dialog?.setCancelable(true)
        dialog?.show()
        val window = dialog?.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    fun initRv() {
        binding.rvHome.layoutManager = LinearLayoutManager(this)
        binding.rvAway.layoutManager = LinearLayoutManager(this)
    }

    fun addDataToHomeRv(teamHomeDtlsArr: JSONArray) {
        val data = ArrayList<MatchDetails>()

        for (i in 0 until teamHomeDtlsArr.length()) {
            val json = JSONObject(teamHomeDtlsArr[i].toString())

            val battingJson = JSONObject(json.getString("Batting"))
            val bowlingJson = JSONObject(json.getString("Bowling"))
            val batting = Batting(
                battingJson.getString("Average"),
                battingJson.getString("Runs"),
                battingJson.getString("Strikerate"),
                battingJson.getString("Style")
            )
            val bowling = Bowling(
                bowlingJson.getString("Average"),
                bowlingJson.getString("Economyrate"),
                bowlingJson.getString("Style"),
                bowlingJson.getString("Wickets")
            )
            var isCaptain = false
            var isKeeper = false
            if (json.has("Iscaptain")) {
                isCaptain = true
            }
            if (json.has("Iskeeper")) {
                isKeeper = true
            }

            val item = MatchDetails(
                batting,
                bowling,
                isCaptain,
                isKeeper,
                json.getString("Name_Full"),
                json.getString("Position")
            )
            data.add(item)
        }

        rvHomeAdapter = CustomAdapter(data)

        binding.rvHome.adapter = rvHomeAdapter

        rvHomeAdapter.setOnItemClickListener {
            showPlayerDetailsDialog(it)
        }
    }

    fun addDataToAwayRv(teamAwayDtlsArr: JSONArray) {
        val data = ArrayList<MatchDetails>()

        for (i in 0 until teamAwayDtlsArr.length()) {
            val json = JSONObject(teamAwayDtlsArr[i].toString())

            val battingJson = JSONObject(json.getString("Batting"))
            val bowlingJson = JSONObject(json.getString("Bowling"))
            val batting = Batting(
                battingJson.getString("Average"),
                battingJson.getString("Runs"),
                battingJson.getString("Strikerate"),
                battingJson.getString("Style")
            )
            val bowling = Bowling(
                bowlingJson.getString("Average"),
                bowlingJson.getString("Economyrate"),
                bowlingJson.getString("Style"),
                bowlingJson.getString("Wickets")
            )
            var isCaptain = false
            var isKeeper = false
            if (json.has("Iscaptain")) {
                isCaptain = true
            }
            if (json.has("Iskeeper")) {
                isKeeper = true
            }

            val item = MatchDetails(
                batting,
                bowling,
                isCaptain,
                isKeeper,
                json.getString("Name_Full"),
                json.getString("Position")
            )
            data.add(item)
        }

        rvAwayAdapter = CustomAdapter(data)

        binding.rvAway.adapter = rvAwayAdapter

        rvAwayAdapter.setOnItemClickListener {
            showPlayerDetailsDialog(it)
        }
    }

    override fun onClick(view: View?) {
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}