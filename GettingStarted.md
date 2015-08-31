目录


# JavaXYQ入门 #

本文介绍如何搭建JavaXYQ的相关环境。





# 下载源代码 #
> ## 下载源码包 ##
> 打开以下地址[Downloads](http://code.google.com/p/javaxyq/downloads/list)，下载源码包(javaxyq-xx-srcxxxx.zip)。<br>
<blockquote>下载完成后将源码包解压到本地磁盘，然后导入到Eclipse中即可。<br>
<i><b>注意</b> ：JavaXYQ的工程类型是Groovy，要先安装Groovy-Eclipse插件，详细请查看下面的<a href='GettingStarted#Groovy-Eclipse.md'>Groovy-Eclipse</a> 。</i></blockquote>

<blockquote><h2>从svn检出</h2>
如果要从源码库中直接检出，请先安装<a href='http://subversion.tigris.org/links.html#clients'>svn客户端工具</a>。<br>
<br>
推荐使用的两个工具：<br>
</blockquote><ul><li><b>TortoiseSVN</b> - A Subversion client, implemented as a windows shell extension。集成到window资源管理器的SVN工具。<br>
<blockquote><a href='http://tortoisesvn.tigris.org/'>http://tortoisesvn.tigris.org/</a>
</blockquote></li><li><b>Subclipse</b> - A Subversion Eclipse Plugin。集成到Eclipse中的SVN插件。<br>
<blockquote><a href='http://subclipse.tigris.org/'>http://subclipse.tigris.org/</a></blockquote></li></ul>

<h1>搭建开发环境</h1>
<blockquote><h2>eclipse3.5</h2>
eclipse下载地址：<a href='http://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/galileo/SR1/eclipse-java-galileo-SR1-win32.zip'>eclipse-java-galileo-SR1-win32.zip</a><br>
下载后，解压到本地目录，然后运行eclipse.exe就可以了。</blockquote>

<blockquote><h2>subeclipse</h2>
安装eclipse的svn插件，这个是可选的。<br>
<a href='http://subclipse.tigris.org/servlets/ProjectProcess?pageID=p4wYuA'>subeclipse下载页面</a><br>
Links for 1.6.x Release:<br>
Changelog: <a href='http://subclipse.tigris.org/subclipse_1.6.x/changes.html'>http://subclipse.tigris.org/subclipse_1.6.x/changes.html</a> <br>
Eclipse update site URL: <a href='http://subclipse.tigris.org/update_1.6.x'>http://subclipse.tigris.org/update_1.6.x</a> <br>
Zipped downloads: <a href='http://subclipse.tigris.org/servlets/ProjectDocumentList?folderID=2240'>http://subclipse.tigris.org/servlets/ProjectDocumentList?folderID=2240</a> <br></blockquote>



<blockquote><h2>Groovy-Eclipse</h2>
<a href='http://groovy.codehaus.org/Install+Groovy-Eclipse+Plugin'>官方安装教程</a><br>
下载地址:  <a href='http://groovy.codehaus.org/Download'>http://groovy.codehaus.org/Download</a> <br></blockquote>


<blockquote><h1>开发JavaXYQ</h1>
<h2>导入工程</h2>
打开Eclipse3.5，并保证安装完成subeclipse和Groovy-eclipse插件。<br>
菜单：File -> Import，选择“SVN -> 从SVN检出项目”。<br>
在从SVN检出的对话框中选择“创建新的资源库位置”，输入地址：<a href='http://javaxyq.googlecode.com/svn/trunk/'>http://javaxyq.googlecode.com/svn/trunk/</a> 。 <br>
在打开的列表中选择'javaxyq'结点，然后点击'Finished'。如果有需要，也可点击'Next'进行定制存放的目录及项目名称。<br></blockquote>


<blockquote><h2>运行JavaXYQ</h2>
游戏入口类：com.javaxyq.core.Main.groovy<br>
运行方式：在Package Explorer中展开src，选择Main.groovy文件，右键点击-Run As，Java Application或者Groovy Application。<br><br></blockquote>

<blockquote>UIMaker : com.javaxyq.tools.UIMaker.groovy<br>
游戏界面生成工具，提供强大的编辑功能，鼠标拖曳、键盘控制，所见即所得。<br><br></blockquote>

<blockquote>ResourceManager : <code>com.javaxyq.tools.ResourceManager.java</code><br>
游戏资源管理器，方便浏览游戏的资源wdf、tcp等，并可以直接导出。<br><br></blockquote>

<blockquote>MapBrowser : <code>com.javaxyq.tools.MapBrowser.java</code><br>
游戏地图查看器，可以查看游戏scene目录下的地图文件(<code>*.map</code>)。<br><br></blockquote>

<blockquote>第一次运行时，会提示某些资源文件找不到，这是因为没有设置好运行的工作目录。<br>
设置Working Directory（工作目录）：菜单'Run' -> 'Run Configurations'，打开后选择Main(如果修改了名字，请选择相应的运行历史)，点击'Arguments'页，在底部可以看到'Working Directory'。默认是'Default'，修改为'Other'，点击'Workspace'，选择工程的res目录，完成后它的值应该是：${workspace_loc:JavaXYQ/res}。最后点击'Apply'按钮，保存设置。<br>
<i>注意：如果导入时修改了工程的名称，这里的值的工程名字将是实际的工程名字。</i>