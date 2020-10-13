package com.android.roundup.dashboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.android.roundup.R
import com.android.roundup.models.Banners
import com.squareup.picasso.Picasso

class AdsAdapter(private val context: Context,bannerimg :List<Banners>) : PagerAdapter() {
    private var layoutInflater: LayoutInflater? = null
    private val images = arrayOf(
        R.drawable.ad,
        R.drawable.ad,
        R.drawable.ad
    )

    var banner:List<Banners> = bannerimg

    override fun getCount(): Int {
        return banner.size
    }

    override fun isViewFromObject(
        view: View,
        `object`: Any
    ): Boolean {
        return view === `object`
    }

    override fun instantiateItem(
        container: ViewGroup,
        position: Int
    ): Any {
        layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view =
            layoutInflater!!.inflate(R.layout.ad_holder, null)
        val imageView =
            view.findViewById<View>(R.id.imageView) as ImageView

      //  imageView.setImageResource(banner[position].bannerimage)
        Picasso.with(context).load("http://trainingscholar.com/studyapp/assets/images/"+banner[position].bannerimage).placeholder(R.drawable.roundup_logo).error(R.drawable.roundup_logo).noFade().into(imageView);


        val vp = container as ViewPager
        vp.addView(view, 0)
        return view
    }

    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        `object`: Any
    ) {
        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)
    }

}