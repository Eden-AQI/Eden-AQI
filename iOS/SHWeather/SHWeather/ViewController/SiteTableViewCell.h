//
//  SiteTableViewCell.h
//  SHWeather
//
//  Created by xiao on 6/14/16.
//  Copyright © 2016 xiao. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SiteTableViewCell : UITableViewCell
//@property (weak, nonatomic) IBOutlet UIImageView *locImageView;
@property (weak, nonatomic) IBOutlet UIView *titleContentView;
@property (weak, nonatomic) IBOutlet UILabel *citylbl;
@property (weak, nonatomic) IBOutlet UILabel *aqilbl;

@end
