package com.example.rss

import android.annotation.TargetApi
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.DialogInterface
import android.graphics.Paint
import android.graphics.PathDashPathEffect
import android.graphics.Typeface
import android.os.Build
import android.os.StrictMode
import android.text.util.Linkify
import android.text.util.Linkify.WEB_URLS
import android.util.Xml
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer
import androidx.documentfile.R
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import org.xml.sax.SAXException
import org.xmlpull.v1.XmlPullParser
import java.io.IOException
import java.io.InputStream
import java.net.URL
import javax.xml.parsers.ParserConfigurationException


class MainActivity : AppCompatActivity() {

    class RssFeedModel(title: String, link: String, description: String) {
        var title = title
        var link = link
        var description = description
    }
    fun parseFeed(inputStream: InputStream): MutableList<RssFeedModel> {
        var title = ""
        var link = ""
        var description = ""
        var isItem = false
        var items: MutableList<RssFeedModel> = mutableListOf(RssFeedModel("no info", "", ""))
        try {
            var xmlPullParser: XmlPullParser = Xml.newPullParser()
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            xmlPullParser.setInput(inputStream, null)
            xmlPullParser.nextTag()
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) { // был next
                var eventType = xmlPullParser.eventType
                var name: String
                name = xmlPullParser.name ?: continue
                if (eventType == XmlPullParser.END_TAG) {
                    if (name != "") {
                        if (name.equals("item", ignoreCase = true)) {
                            isItem = false
                        }
                    }
                }
                if (eventType == XmlPullParser.START_TAG) {
                    if (name.equals("item", ignoreCase = true)) {
                        isItem = true
                    }
                }
                var result = ""
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.text
                    //xmlPullParser.nextTag()
                }
                /*} else {
                        items.add(RssFeedModel("", "", ""))*/
                when {
                    name.equals("title", true) -> {
                        title = result
                        if (xmlPullParser.next() == XmlPullParser.TEXT) {
                            result = xmlPullParser.text
                        }
                    }
                    name.equals("link", true) -> {
                        link = result
                        if (xmlPullParser.next() == XmlPullParser.TEXT) {
                            result = xmlPullParser.text
                        }
                    }
                    name.equals("description", true) -> {
                        description = result
                        if (xmlPullParser.next() == XmlPullParser.TEXT) {
                            result = xmlPullParser.text
                        }
                    }
                }
                if ((title != "") && (link != "") && (description != "")) {
                    if (isItem) {
                        items.add(RssFeedModel(title, link, description))
                        isItem = false
                    }

                    title = ""
                    link = ""
                    description = ""
                }
                //xmlPullParser.next()
            }

        }
        catch(e: Exception) {
            items = listOf(RssFeedModel(e.toString(), "", "")).toMutableList()
        }
        finally {
            inputStream.close()
            return items
        }
    }

    fun rssView(finalText: TextView): List<RssFeedModel> {
        var rssUrl: URL
        var parsedList: MutableList<RssFeedModel>
        try {
            rssUrl = URL(finalText.text.toString())
            var inputSource: InputStream = rssUrl.openConnection().getInputStream()
            parsedList = parseFeed(inputSource)

        } catch (e: Exception) {
            parsedList = mutableListOf(RssFeedModel(e.toString(), "", ""))
        }
        return parsedList
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.rss.R.layout.activity_main) // без com

        val policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val button: Button = findViewById(com.example.rss.R.id.addRSSButton) // без com

        //var feedModelList: List<RssFeedModel>


        button.setOnClickListener {
            val li: LayoutInflater = LayoutInflater.from(this)
            var promptView = li.inflate(com.example.rss.R.layout.prompt, null) //без com
            var iLayout: LinearLayout = findViewById(com.example.rss.R.id.rssScrollLayout) // без com
            var mBuilder = AlertDialog.Builder(this)
            mBuilder.setView(promptView)
            var userUrlInput = promptView.findViewById<EditText>(com.example.rss.R.id.urlRSS) //без com

            val positiveButtonClick = { dialog: DialogInterface,
                                        _: Int ->
                dialog.cancel()
                try{
                    var parsedList = rssView(finalText = userUrlInput)
                    for (i in 1 until parsedList.size) { // 2
                        var rowTitle = TextView(this)
                        var rowDescription = TextView(this)
                        var rowLink = TextView(this)
                        //if (rowLink.text.contains((regex))) {System.out.println("yooo")}
                        //var list = mutableListOf(rowTitle, rowDescription, rowLink)

                        rowTitle.text = "$i) " + parsedList[i].title // +title +desc + link
                        rowTitle.typeface = Typeface.DEFAULT_BOLD
                        //rowTitle.fontFeatureSettings = "'color' 00FF00"
                        rowDescription.text = parsedList[i].description
                        rowLink.text = parsedList[i].link
                        Linkify.addLinks(rowLink, WEB_URLS)
                        iLayout.addView(rowTitle)
                        iLayout.addView(rowDescription)
                        iLayout.addView(rowLink)
                        welcomeText.setText("")
                    }

                }catch(e: Exception){
                    var exceptionText = TextView(this)
                    exceptionText.text = e.toString()
                    iLayout.addView(exceptionText)}
            }

            mBuilder.setCancelable(true)
            mBuilder.setPositiveButton("ok", DialogInterface.OnClickListener(function = positiveButtonClick))
            val mAlertDialog: AlertDialog = mBuilder.create()
            mAlertDialog.show()
        }
    }
}

