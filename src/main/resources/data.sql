INSERT INTO USERS (id, password, email, username, active)
VALUES
  (1, '$2a$06$OAPObzhRdRXBCbk7Hj/ot.jY3zPwR8n7/mfLtKIgTzdJa4.6TwsIm', 'user@mail.com', 'user', 1);
-- password in plaintext: "password"
INSERT INTO USERS (id, password, email, username, active)
VALUES
  (2, '$2a$06$OAPObzhRdRXBCbk7Hj/ot.jY3zPwR8n7/mfLtKIgTzdJa4.6TwsIm', 'johndoe@gmail.com', 'johndoe', 1);
-- password in plaintext: "password"
INSERT INTO USERS (id, password, email, username, active)
VALUES (3, '$2a$06$OAPObzhRdRXBCbk7Hj/ot.jY3zPwR8n7/mfLtKIgTzdJa4.6TwsIm', 'ana@mail.com', 'ana', 1);

-- Roles
INSERT INTO ROLES (id, role)
VALUES (1, 'ROLE_ADMIN');
INSERT INTO ROLES (id, role)
VALUES (2, 'ROLE_USER');

-- User Roles
INSERT INTO USER_ROLES (user_id, role_id)
VALUES (1, 1);
INSERT INTO USER_ROLES (user_id, role_id)
VALUES (1, 2);
INSERT INTO USER_ROLES (user_id, role_id)
VALUES (2, 2);
INSERT INTO USER_ROLES (user_id, role_id)
VALUES (3, 2);

-- Posts
INSERT INTO POSTS (id, title, post_content, isprivate)
VALUES (1, 'Title 1',
        'Loremt', 0);
INSERT INTO POSTS (id, title, post_content, isprivate)
VALUES (2, 'Title 2',
        'Lorem2', 1);
INSERT INTO POSTS (id, title, post_content, isprivate)
VALUES (3, 'Title 3',
        'Lorem3', 0);
INSERT INTO POSTS (id, title, post_content, isprivate)
VALUES (4, 'Title 4',
        'Lorem4', 1);

INSERT INTO POST_AUTHORS (post_id, user_id)
VALUES (1, 1);
INSERT INTO POST_AUTHORS (post_id, user_id)
VALUES (1, 2);
INSERT INTO POST_AUTHORS (post_id, user_id)
VALUES (2, 2);
INSERT INTO POST_AUTHORS (post_id, user_id)
VALUES (3, 2);
INSERT INTO POST_AUTHORS (post_id, user_id)
VALUES (4, 3);


INSERT INTO TAGS (id, name)
VALUES (1, 'tag1');
INSERT INTO TAGS (id, name)
VALUES (2, 'tag2');
INSERT INTO TAGS (id, name)
VALUES (3, 'tag3');
INSERT INTO TAGS (id, name)
VALUES (4, 'tag4');

INSERT INTO POST_TAGS (post_id, tag_id)
VALUES (1, 1);
INSERT INTO POST_TAGS (post_id, tag_id)
VALUES (1, 2);
INSERT INTO POST_TAGS (post_id, tag_id)
VALUES (2, 2);
INSERT INTO POST_TAGS (post_id, tag_id)
VALUES (3, 3);


-- Comments
INSERT INTO COMMENTS (id, post_id, user_id, body)
VALUES (1, 1, 1,
        'Lorem');
INSERT INTO COMMENTS (id, post_id, user_id, body)
VALUES (2, 2, 2,
        'Lorem2');
INSERT INTO COMMENTS (id, post_id, user_id, body)
VALUES (3, 3, 3,
        'Lorem3 ');
