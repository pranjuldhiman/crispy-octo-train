package com.rs.roundupclasses.examcontent.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rs.roundupclasses.R
import com.rs.roundupclasses.dashboard.adapter.VideoItemDecoration
import com.rs.roundupclasses.pdf.PdfViewActivity
import com.rs.roundupclasses.utils.Constants
import com.rs.roundupclasses.utils.RoundUpHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_pdf_list.*

class PdfFragment : Fragment()
{
    lateinit var sharedPreferences: SharedPreferences

    private val viewModel by lazy {
        ViewModelProviders.of(this, RoundUpHelper.viewModelFactory { PdfFragmentViewModel(activity as AppCompatActivity) }).get(
            PdfFragmentViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pdf_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedPreferences = (activity as AppCompatActivity).getSharedPreferences(Constants.SHAREDPREFERENCEFILE, Context.MODE_PRIVATE)
        var topic_id = sharedPreferences.getString(Constants.TOPICID,"")
        var userid: String= sharedPreferences.getString(Constants.USERID,"").toString()

        viewModel.getVideoData(userid,topic_id!!)
         setViewModelObservers()

        /*rcv_exam_component.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = VideoAdapter({

            }, context, emptyList())
            addItemDecoration(VideoDecoration(40))
        }*/
    }

    private fun setViewModelObservers() {
        viewModel.apply {
            listOfSearchResult.observe(viewLifecycleOwner, Observer {
                pdflist.visibility = View.VISIBLE
                pdflist.apply {
                    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    adapter = PdfAdapter({ s: String, url: String ->
                        //   val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://file-examples-com.github.io/uploads/2017/10/file-sample_150kB.pdf"))
                        //  val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://marketlens.in/assets/video/20200918163242_market_lens_course_.pdf_files.pdf"))
                      //  startActivity(myIntent)
                        val intent = Intent(context, PdfViewActivity::class.java)
                        intent.putExtra("ViewType", s)
                        intent.putExtra("URL", url)
                        startActivity(intent)

                        /*  if(it.having_subcategory.equals("1"))
                          {
                              var list:ArrayList<Subcategory> = it.subcategory as ArrayList<Subcategory>
                              //   context.startActivity(Intent(context, ChooseIconActivity::class.java))
                              val intent = Intent(context, ChooseIconActivity::class.java)
  //                            val b = Bundle()
  //                            b.putSerializable("questions", list)
                              intent.putExtra("type", it.category)
                              intent.putExtra("LIST", list)
                              startActivity(intent)
                          }
                          else
                          {
                              context.startActivity(Intent(context, ExamContentActivity::class.java))
                          }*/
                    }, context, it.orEmpty())
                    addItemDecoration(VideoItemDecoration(10))
                }
            })
            serviceException.observe(viewLifecycleOwner, Observer {
                rcv_dashboard.visibility = View.GONE
                showMessageDialog(it.orEmpty())
            })
        }
    }

    private fun showMessageDialog(msgWrapper: String?) {
        msgWrapper?.let {
            val msg = it
            AlertDialog.Builder(activity as AppCompatActivity)
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    //startActivity(Intent(this, ScanActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                }
                .create().run {
                    setCanceledOnTouchOutside(false)
                    show()
                }
        }
    }
}