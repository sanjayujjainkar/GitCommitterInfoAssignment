package com.pof.gitcommitterinfo.data.model;

public class GitCommit
{
    private Committer committer;

    private Commit commit;

    private String sha;

    public Committer getCommitter ()
    {
        return committer;
    }

    public void setCommitter (Committer committer)
    {
        this.committer = committer;
    }

    public Commit getCommit ()
    {
        return commit;
    }

    public void setCommit (Commit commit)
    {
        this.commit = commit;
    }

    public String getSha ()
    {
        return sha;
    }

    public void setSha (String sha)
    {
        this.sha = sha;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [committer = "+committer+", commit = "+commit+", sha = "+sha+"]";
    }

    public class Committer {
        private String login;
        private String avatar_url;
        private String html_url;
        private String url;

        public String getLogin()
        {
            return login;
        }

        public void setLogin(String login)
        {
            this.login = login;
        }

        public String getAvatar_url()
        {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url)
        {
            this.avatar_url = avatar_url;
        }

        public String getHtml_url()
        {
            return html_url;
        }

        public void setHtml_url(String html_url)
        {
            this.html_url = html_url;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [login = "+ login +", avatar_url = "+ avatar_url +", html_url = "+ html_url +"]";
        }
    }

    public class Commit
    {
        private Committer committer;

        private String message;

        public Committer getCommitter ()
        {
            return committer;
        }

        public void setCommitter (Committer committer)
        {
            this.committer = committer;
        }

        public String getMessage ()
        {
            return message;
        }

        public void setMessage (String message)
        {
            this.message = message;
        }


        public class Committer {

            private String name;
            private String email;
            private String date;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }
        }

    }

}

