-- This won't affect existing tables
CREATE TABLE IF NOT EXISTS video_like (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    video_id BIGINT,
    created_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (video_id) REFERENCES video(id)
); 