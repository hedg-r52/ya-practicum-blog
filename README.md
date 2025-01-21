# Task "Blog" by Yandex.Practicum

Creation an application blog using Spring Framework with the addition of tests.

### Requirements and functionality:
- Web application written using Spring Framework (version 6.1 and above), performed in any of modern servlet-containers (Jetty or Tomcat)
- Maven or Gradle assembly system.
- JDK 21.
- The database of the application can be Persistent(e.g. PostgreSQL) or in memory (like, H2)
- The blog should consist of two web pages (HTML + JS): post list, post page.
- In the list of posts there are:
  - post preview (name, image, briefly the first paragraph no more than three lines);
  - the quantity of comments on the post;
  - the quantity of likes to the post;
  - posts tags;
  - posts are displayed from top to bottom;
  - filtering by tag;
  - pagination (10, 20, 50 posts on the page).
- In the list of posts there is a button for adding a post,when you press the form of adding a post with capabilities:
  - adding the name of the post;
  - adding a picture;
  - writing text;
  - the arrangements of tags.
- When you press the name of the post (should be a link)? the page of the post on which there are:
  - name of the post;
  - image;
  - the text of the post, divided into paragraphs;
  - post tags;
  - delete button and edit button (edition of the post is similar to its adding);
  - add comment button;
  - like button for the post, when pressing on the counter increases by one;
  - a list of comments (tree of comments or hierarchy are not necessary);
  - each comment contains the text of the comment and the possibility of editing/deleting it.
- Adding or Editing comment on the current page. When clicking on a comment, its text is replaced on the text input field, when clicking on Ctrl+Enter, the comment is saved.
- The application should be covered with test (unit and integration) using JUnit 5, TestContext Framework and context caching.
