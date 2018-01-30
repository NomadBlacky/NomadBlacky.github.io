// Dependencies
import $ivy.`org.pegdown:pegdown:1.6.0`
import $ivy.`com.lihaoyi::scalatags:0.6.7`

import $file.src.pages, pages._

import ammonite.ops._
import org.pegdown.PegDownProcessor
import scalatags.Text.all._
import scalatags.Text.tags2

val pagesDir  = pwd/'pages
val postsDir = pwd/'posts

val pegdown = new PegDownProcessor()

@main
def main() = {
  val (markdowns, otherFiles) = ls! postsDir partition (_.ext == "md")
  val dests = markdowns
    .map(m => (m, pegdown.markdownToHtml(read! m)))
    .map { case (m, h) =>
      val page = html(
        head(
          tags2.title(m.last)
        ),
        body(
          raw(h)
        )
      )
      write.over(pagesDir/(m.last + ".html"), page.toString)
    }

  write.over(pwd/"index.html", indexPageBuilder(pwd/"index.md").rawString)

  write.over(pwd/'pages/"blogs.html", postsListPageBuilder(markdowns).rawString)

  println("Complete!")
}
