//
//  RankTableViewCell.h
//  SHWeather
//
//  Created by xiao on 6/13/16.
//  Copyright © 2016 xiao. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface RankTableViewCell : UITableViewCell
//@property (weak, nonatomic) IBOutlet UILabel *ranklbl;
//@property (weak, nonatomic) IBOutlet UIImageView *rankImageView;
@property (weak, nonatomic) IBOutlet UILabel *citylbl;
@property (weak, nonatomic) IBOutlet UILabel *AQIlbl;

- (void)loadDataWith:(NSString *)cityName;

@end
