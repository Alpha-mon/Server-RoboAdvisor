-- Junit5 Test 시, Auto increment 초기화를 위한 .sql 스크립트
SET foreign_key_checks = 0;
TRUNCATE TABLE comments;
TRUNCATE TABLE posts;
SET foreign_key_checks = 1;
