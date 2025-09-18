package SRMovieDB.Service;

import org.springframework.stereotype.Service;

@Service
public class MovieService {

    private String SessionId;
    private String RequestToken;
    private long AccountId;

    public String getRequestToken() {
        return RequestToken;
    }

    public void setRequestToken(String RequestToken) {
        this.RequestToken = RequestToken;
    }

    public long getAccountId() {
        return AccountId;
    }

    public void setAccountId(long AccountId) {
        this.AccountId = AccountId;
    }

    public String getSessionId() {
        return SessionId;
    }

    public void setSessionId(String SessionId) {
        this.SessionId = SessionId;

    }

    public boolean hasSession() {
        return this.SessionId != null && !this.SessionId.isEmpty();
    }

    public void clearSession() {
        this.SessionId = null;
        this.RequestToken = null;
        this.AccountId = 0;
    }

}
