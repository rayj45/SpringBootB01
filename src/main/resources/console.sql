select now();

SELECT * FROM BOARD
ORDER BY BNO DESC;

SHOW TABLE STATUS LIKE 'BOARD'; /*data dictionary*/

ALTER TABLE BOARD AUTO_INCREMENT= 1; /*auto increment 초기화*/

SELECT * FROM REPLY
ORDER BY RNO DESC;

SELECT COUNT(*) FROM REPLY;

INSERT INTO REPLY (moddate, regdate, reply_text, replyer, board_bno)
SELECT moddate, regdate, reply_text, replyer, board_bno from REPLY;

SELECT * FROM BOARD_IMAGE
where board_bno = 101;

SELECT * FROM BOARD_IMAGE_SET;

DROP TABLE BOARD;

DROP TABLE REPLY;

DROP TABLE BOARD_IMAGE;

DROP TABLE BOARD_IMAGE_SET;