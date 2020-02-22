package com.example.myapplication.ui.webView

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import kotlinx.android.synthetic.main.main.*


class WebViewFragment : Fragment() {

    companion object {
        fun newInstance() = WebViewFragment()

        val TAG = WebViewFragment::class.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url = MainActivity.url
        webView.settings.domStorageEnabled = true
        webView.settings.useWideViewPort = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.setSupportZoom(true)
        webView.settings.builtInZoomControls = true
        webView.webChromeClient = WebChromeClient()
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(url)

        Log.d(TAG, "onViewCreated")
        Log.d(TAG, "Loaded $url")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (this.activity as? AppCompatActivity)?.supportActionBar?.let {
            it.hide()
            it.title = Html.fromHtml("<font color=\"#FBC81D\">" + getString(R.string.app_name) + "</font>")
        }
    }


}