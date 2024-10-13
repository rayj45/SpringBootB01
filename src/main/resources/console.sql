select now();

SELECT * FROM BOARD
ORDER BY BNO DESC;

SELECT * FROM REPLY
ORDER BY RNO DESC;

SELECT COUNT(*) FROM REPLY;

INSERT INTO REPLY (moddate, regdate, reply_text, replyer, board_bno)
SELECT moddate, regdate, reply_text, replyer, board_bno from REPLY;