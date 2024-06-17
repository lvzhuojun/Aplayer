package remix.myplayer.ui.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import remix.myplayer.bean.misc.Library
import remix.myplayer.ui.activity.RemoteFragment
import remix.myplayer.ui.fragment.*
import java.lang.IllegalArgumentException
import java.lang.ref.WeakReference

class MainPagerAdapter(private val fm: FragmentManager) : FragmentPagerAdapter(fm) {
  private var libraries: List<Library> = ArrayList()
  private val fragmentMap: MutableMap<Int, WeakReference<Fragment?>> = HashMap()

  override fun getItemId(position: Int): Long {
    return if (position >= libraries.size) {
      super.getItemId(position)
    } else libraries[position].tag.toLong()
  }

  override fun getItemPosition(obj: Any): Int {
    for (i in libraries.indices) {
      if (libraries[i].className == obj.javaClass.name) {
        return i
      }
    }
    return POSITION_NONE
  }

  override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
    super.destroyItem(container, position, `object`)
    val weakReference = fragmentMap[position]
    weakReference?.clear()
  }

  override fun instantiateItem(container: ViewGroup, position: Int): Any {
    val fragment = super.instantiateItem(container, position) as Fragment
    val weakReference = fragmentMap[position]
    weakReference?.clear()
    fragmentMap[position] = WeakReference(fragment)
    return fragment
  }

  fun getFragment(position: Int): Fragment? {
    if (position >= libraries.size) {
      return Fragment()
    }
    val (_, _, className) = libraries[position]
    for (fragment in fm.fragments) {
      if (fragment is LibraryFragment<*, *, *> && (fragment.javaClass.name == className)) {
        return fragment
      }
    }
    val weakReference = fragmentMap[position]
    return if (weakReference?.get() != null) {
      weakReference.get()
    } else getItem(position)
  }

  override fun getItem(position: Int): Fragment {
    if (position >= libraries.size) {
      return Fragment()
    }
    val weakReference = fragmentMap[position]
    if (weakReference?.get() != null) {
      return weakReference.get()!!
    }
    val (tag) = libraries[position]

    val fragment = when (tag) {
      Library.TAG_SONG -> SongFragment()
      Library.TAG_ALBUM -> AlbumFragment()
      Library.TAG_ARTIST -> ArtistFragment()
      Library.TAG_PLAYLIST -> PlayListFragment()
      else -> throw IllegalArgumentException("unknown library tag in getItem: $tag")
    }
    val newWeakReference = WeakReference(fragment as Fragment)
    fragmentMap[position] = newWeakReference
    return fragment
  }

  private fun alignCache() {
    if (fragmentMap.isEmpty()) {
      return
    }
    val mapds = HashMap<String, WeakReference<Fragment?>>(fragmentMap.size)
    run {
      var i = 0
      val size = fragmentMap.size
      while (i < size) {
        val ref = fragmentMap[i]
        if (ref?.get() != null) {
          mapds[ref.get()!!.javaClass.name] = ref
        }
        i++
      }
    }
    var i = 0
    val size = libraries.size
    while (i < size) {
      val ref = mapds[libraries[i].className]
      if (ref != null) {
        fragmentMap[i] = ref
      } else {
        fragmentMap.remove(i)
      }
      i++
    }
  }

  var list: List<Library>
    get() = libraries
    set(categories) {
      // 过滤掉无效的 tag 值
      libraries = categories.filter { it.tag in Library.TAG_SONG..Library.TAG_PLAYLIST }
      alignCache()
    }

  override fun getPageTitle(position: Int): CharSequence {
    return libraries[position].getTitle()
  }

  override fun getCount(): Int {
    return libraries.size
  }
}
