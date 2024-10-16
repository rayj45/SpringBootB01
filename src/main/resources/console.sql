select now();

SELECT * FROM BOARD
ORDER BY BNO DESC;

SELECT * FROM REPLY
ORDER BY RNO DESC;

SELECT COUNT(*) FROM REPLY;

INSERT INTO REPLY (moddate, regdate, reply_text, replyer, board_bno)
SELECT moddate, regdate, reply_text, replyer, board_bno from REPLY;

SELECT * FROM BOARD_IMAGE;

SELECT * FROM BOARD_IMAGE_SET;

DROP TABLE BOARD;

DROP TABLE REPLY;

DROP TABLE BOARD_IMAGE;

DROP TABLE BOARD_IMAGE_SET;