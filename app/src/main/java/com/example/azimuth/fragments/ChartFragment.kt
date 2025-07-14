package com.example.azimuth.fragments

import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.azimuth.CardAdapter
import com.example.azimuth.R
import com.example.azimuth.SQLiteHelper
import com.example.azimuth.databinding.FragmentChartBinding
import com.sjapps.library.customdialog.CustomViewDialog

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ChartFragment : Fragment() {
    private var _binding: FragmentChartBinding? = null
    private val binding get() = _binding!!

    private lateinit var mSQLiteHelper: SQLiteHelper
    private lateinit var mSQLiteDatabase: SQLiteDatabase
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var machineData: Triple<String?, String?, String?>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChartBinding.inflate(inflater, container, false)

        mSQLiteHelper = SQLiteHelper.getInstance(context)
        mSQLiteDatabase = mSQLiteHelper.writableDatabase
        mRecyclerView = binding.listMachine
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        val itemDecor = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        mRecyclerView.addItemDecoration(itemDecor)
        readDatabaseToRecyclerview()

        binding.btnAddMachine.setOnClickListener(){
            showDialogAddDevice()
        }


        return binding.root
    }

    fun readDatabaseToRecyclerview() {
        val sql = "SELECT * FROM machine ORDER BY timestamp DESC"
        val cursor = mSQLiteDatabase.rawQuery(sql, null)
        val adapter = CardAdapter(cursor)
        mRecyclerView.adapter = adapter
    }

    private fun showDialogAddDevice(){
        val view = layoutInflater.inflate(R.layout.dialog_add_device, null)
        val machineName = view.findViewById<EditText>(R.id.machine_name)
        val machineClientID = view.findViewById<EditText>(R.id.machine_clientID)
        val machineToken = view.findViewById<EditText>(R.id.machine_token)

        val customViewDialog = CustomViewDialog()
        customViewDialog.Builder(context).apply {
            setTitle("ADD MACHINE")
            dialogWithTwoButtons()
            addCustomView(view)
            setLeftButtonColor(Color.parseColor("#FF0000"))
            setRightButtonColor(Color.parseColor("#008000"))
            onButtonClick {
                if (machineName != null && machineClientID != null && machineToken != null){
                    machineData = Triple(machineName.text.toString(), machineClientID.text.toString(), machineToken.text.toString())
                    insertDatabase()
                }
                dismiss()
            }
            show()

        }
//        AlertDialog.Builder(context).apply {
//            setTitle("Add Machine")
//            setView(view)
//            setPositiveButton("OK"){ _, _ ->
//            }
//            setNegativeButton("Cancel"){ _, _ ->
//                onInputDialogReturn(null)
//            }
//            show()
//        }
    }

    private fun insertDatabase() {
        var now = System.currentTimeMillis()
        var sql = """INSERT INTO machine (_id, name, clientID, token, timestamp) VALUES (?, ?, ?, ?, ?)"""
        var args = arrayOf(null, machineData.first, machineData.second, machineData.third, now)
        mSQLiteDatabase.execSQL(sql, args)
        readDatabaseToRecyclerview()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}