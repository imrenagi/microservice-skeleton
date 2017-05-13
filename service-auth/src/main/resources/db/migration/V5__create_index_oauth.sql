CREATE INDEX token_id_index ON oauth_access_token (token_id);
CREATE INDEX authentication_id_index ON oauth_access_token (authentication_id);
CREATE INDEX refresh_token_index ON oauth_access_token (refresh_token);
CREATE INDEX user_name_index ON oauth_access_token (user_name);
CREATE INDEX client_id_index ON oauth_access_token (client_id);

CREATE INDEX token_id_index ON oauth_client_token(token_id);
CREATE INDEX authentication_id_index ON oauth_client_token (authentication_id);

CREATE INDEX token_id_index ON oauth_refresh_token (token_id);

CREATE INDEX client_id_index ON oauth_client_details(client_id);

CREATE INDEX code_index ON oauth_code (code);
