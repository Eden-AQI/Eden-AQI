//
//  RankTableViewCell.m
//  SHWeather
//
//  Created by xiao on 6/13/16.
//  Copyright © 2016 xiao. All rights reserved.
//

#import "RankTableViewCell.h"

@implementation RankTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void)loadDataWith:(NSString *)cityName
{
    self.citylbl.text = cityName;
}

@end
