package com.android.roundup.examcontent

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.android.roundup.R
import com.android.roundup.examcontent.fragments.PdfFragment
import com.android.roundup.examcontent.fragments.VideoFragment

class ExamPagerAdapter(context: Context, fm: FragmentManager, behavior: Int):
    FragmentStatePagerAdapter(fm, behavior){

    private val mFragmentList = ArrayList<Fragment>()
    private val mFragmentTitleList = ArrayList<String>()
    init {

        mFragmentTitleList.apply {
            add(context.getString(R.string.video))
            add(context.getString(R.string.pdf))
        }
        
        mFragmentList.apply {
            add(VideoFragment())
            add(PdfFragment())
        }

    }
    override fun getItem(position: Int): Fragment {
       return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList[position]
    }
}
