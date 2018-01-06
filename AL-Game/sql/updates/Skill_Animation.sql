ALTER TABLE `player_skills` ADD COLUMN skill_animation INT(11) NOT NULL AFTER `skill_level`;
ALTER TABLE `player_skills` ADD COLUMN skill_animation_enabled INT(1) NOT NULL AFTER `skill_animation`;