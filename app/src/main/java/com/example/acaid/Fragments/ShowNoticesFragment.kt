package com.example.acaid.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.acaid.Adapters.ShowNoticesAdapter
import com.example.acaid.Models.Notice
import com.example.acaid.R
import com.example.acaid.databinding.FragmentShowNoticesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ShowNoticesFragment : Fragment() {
    private var _binding: FragmentShowNoticesBinding? = null
    private val binding get() = _binding!!
    private val db= FirebaseFirestore.getInstance()
    private val auth= FirebaseAuth.getInstance()
    val noticesList= mutableListOf<Notice>()
    private val classIdMap = mapOf(
        "BSSE Regular 1 (2025-2029) - Semester 1" to "BSR25S1",
        "BSSE Self Support 2 (2023-2027) - Semester 4" to "BSS23S4",
        "BSSE Self Support 2 (2022-2026) - Semester 6" to "BSS22S6",
        "BSSE Regular 1 (2024-2028) - Semester 2" to "BSR24S2",
        "BSSE Self Support 1 (2022-2026) - Semester 6" to "BSS22S6",
        "BSSE Self Support 2 (2024-2028) - Semester 2" to "BSS24S2",
        "BSSE Regular 1 (2022-2026) - Semester 6" to "BSR22S6",
        "BSSE Self Support 1 (2021-2025) - Semester 8" to "BSS21S8",
        "BSSE Regular 1 (2021-2025) - Semester 8" to "BSR21S8",
        "BSSE Self Support 1 (2023-2027) - Semester 4" to "BSS23S4",
        "BSSE Regular 1 (2023-2027) - Semester 4" to "BSR23S4",
        "BSSE Regular 1 (2024-2028) - Semester 3" to "BSR24S3",
        "BSSE Self Support 1 (2024-2028) - Semester 2" to "BSS24S2",
        "MSSE Self Support 1 (2024-2026) - Semester 2" to "MSS24S2",
        "BSSE Self Support 1 (2025-2029) - Semester 1" to "BSS25S1"
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowNoticesBinding.inflate(inflater, container, false)
        db.collection("users").document(auth.currentUser!!.uid).get().addOnSuccessListener {
            if(it.exists()){
                val className=it.getString("class")
                val classId=classIdMap[className]
                db.collection("classes").document(classId!!).collection("notices")
                      .get()
                      .addOnSuccessListener { document->
                          val list=document.toObjects(Notice::class.java)
                          if (list.isEmpty()) {
                              showEmptyState()
                          } else {
                              noticesList.addAll(list)
                              binding.apply {
                                  noticeContainer.visibility = View.GONE
                                  showNoticesRecyclerView.visibility = View.VISIBLE
                                  showNoticesRecyclerView.adapter = ShowNoticesAdapter(noticesList)
                                  showNoticesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                              }
                          }



                      }
                      .addOnFailureListener {
                          it.printStackTrace()
                          }

                      }

            }

        return binding.root
    }

    private fun showEmptyState() {
        binding.noticeContainer.visibility=View.VISIBLE
        binding.showNoticesRecyclerView.visibility=View.GONE

    }

}