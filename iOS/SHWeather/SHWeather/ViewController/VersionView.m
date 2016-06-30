//
//  VersionView.m
//  SHWeather
//
//  Created by xiao on 6/27/16.
//  Copyright © 2016 xiao. All rights reserved.
//

#import "VersionView.h"
#import "LevelInfoCell.h"

@interface VersionView ()<UITableViewDelegate,UITableViewDataSource>

@property (nonatomic, strong) NSArray *keys;
@property (nonatomic, strong) NSDictionary *infoDic;
@property (nonatomic, strong) NSDictionary *titleDic;

@property (nonatomic, strong) NSDictionary *gradeInfoDic;
@property (nonatomic) BOOL islevelTable;

@property (weak, nonatomic) IBOutlet UILabel *titleLbl;
@property (weak, nonatomic) IBOutlet UIImageView *aboutView;
@property (weak, nonatomic) IBOutlet UILabel *namelbl;
@property (weak, nonatomic) IBOutlet UILabel *deslbl;
@property (weak, nonatomic) IBOutlet UIImageView *iconImage;
@property (weak, nonatomic) IBOutlet UITableView *dataTable;

@end

@implementation VersionView

+(VersionView *)createViewWithType:(NSString *)type
{
    NSArray *nibs=[[NSBundle mainBundle]loadNibNamed:@"VersionView" owner:nil options:nil];
    VersionView *theView=[nibs objectAtIndex:0];
    if ([type isEqualToString:@"version"]) {
        theView.titleLbl.text = @"版本信息";
        [theView aboutViewsAlpha:0];
        theView.dataTable.alpha = 1.0;
        [theView checkVersion];
    }else if ([type isEqualToString:@"about"]){
        theView.titleLbl.text = @"关于我们";
        NSDictionary *infoDict = [[NSBundle mainBundle] infoDictionary];
        float currentVersion = [[infoDict objectForKey:@"CFBundleVersion"] floatValue];
        theView.namelbl.text = [NSString stringWithFormat:@"郑州空气质量 %.1f",currentVersion];
        theView.dataTable.alpha = 0;
        [theView aboutViewsAlpha:1.0];
    }else{
        theView.titleLbl.text = @"空气质量等级";
        theView.islevelTable = YES;
        theView.dataTable.alpha = 1.0;
        [theView aboutViewsAlpha:0];
        theView.aboutView.alpha = 1.0;
        theView.dataTable.backgroundColor = [UIColor clearColor];
        theView.dataTable.separatorStyle = UITableViewCellSeparatorStyleNone;
        [theView assembleData];
    }

    return theView;
}

- (void)aboutViewsAlpha:(float)alpha
{
    self.aboutView.alpha = alpha;
    self.iconImage.alpha = alpha;
    self.namelbl.alpha = alpha;
    self.deslbl.alpha = alpha;
}

- (BOOL)checkIfHasNewVersion
{
    BOOL hasNewVersion = NO;
    NSDictionary *infoDict = [[NSBundle mainBundle] infoDictionary];
    NSString *currentVersion = [infoDict objectForKey:@"CFBundleVersion"];
    if ([currentVersion floatValue] != [[self.infoDic objectForKey:@"VersionCode"] floatValue]) {
        self.titleLbl.text = @"检查到更新版本";
        hasNewVersion = YES;
    }else{
        self.titleLbl.text = @"当前是最新版本";
    }
    return hasNewVersion;
}

- (void)assembleData
{
    self.gradeInfoDic = [DefaultsHandler sharedInstance].gradeInfo;
    self.dataTable.delegate = self;
    self.dataTable.dataSource = self;
    [self.dataTable reloadData];
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    if (self.islevelTable) {
        return self.gradeInfoDic.allKeys.count;
    }
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (self.islevelTable) {
        return 4;
    }
    return self.keys.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (self.islevelTable) {
        NSDictionary *itemDic = [self.gradeInfoDic objectForKey:[NSNumber numberWithInteger:indexPath.section+1]];
        if (indexPath.row == 2) {
            NSString *str = [itemDic objectForKey:@"HealthEffect"];
            CGSize size = CGSizeMake(self.dataTable.bounds.size.width-24, 1000);
            CGSize labelSize = [str boundingRectWithSize:size options:NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName: [UIFont systemFontOfSize:18]} context:NULL].size;
            return labelSize.height+20;
        }
        if (indexPath.row == 3) {
            NSString *str = [itemDic objectForKey:@"Method"];
            CGSize size = CGSizeMake(self.dataTable.bounds.size.width-24, 1000);
            CGSize labelSize = [str boundingRectWithSize:size options:NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName: [UIFont systemFontOfSize:18]} context:NULL].size;
            return labelSize.height+20;
        }
        return 30;
    }
    return 44;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        if (self.islevelTable) {
            NSArray *nibs=[[NSBundle mainBundle]loadNibNamed:@"LevelInfoCell" owner:nil options:nil];
            if (nibs.count>0) {
                for (id view in nibs) {
                    if ([view isMemberOfClass:[LevelInfoCell class]]) {
                        cell=(LevelInfoCell *)view;
                    }
                }
            }
        }else
            cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier] ;
    }
    if (self.islevelTable) {
        LevelInfoCell *levelCell = (LevelInfoCell *)cell;
        levelCell.selectionStyle = UITableViewCellSelectionStyleNone;
        levelCell.backgroundColor = [UIColor clearColor];
        if (indexPath.row<2) {
            levelCell.infolbl.textColor = [DefaultsHandler getColorOrderLevel:[NSString stringWithFormat:@"%ld",indexPath.section+1]];
        }else{
            levelCell.infolbl.textColor = [UIColor whiteColor];
        }
        NSString *infoString = nil;
        NSDictionary *itemDic = [self.gradeInfoDic objectForKey:[NSNumber numberWithInteger:indexPath.section+1]];
        switch (indexPath.row) {
            case 0:
                infoString = [itemDic objectForKey:@"AQIState"];
                break;
            case 1:
                infoString = [NSString stringWithFormat:@"空气质量指数：%@～%@",[itemDic objectForKey:@"AQIMin"],[itemDic objectForKey:@"AQIMax"]];
                break;
            case 2:
                infoString = [NSString stringWithFormat:@"对健康影响情况：%@",[itemDic objectForKey:@"HealthEffect"]];
                break;
            case 3:
                infoString = [NSString stringWithFormat:@"建议采取的措施：%@",[itemDic objectForKey:@"Method"]];
                break;
            default:
                break;
        }
        levelCell.infolbl.text = infoString;
    }else{
        NSString *key = [self.keys objectAtIndex:indexPath.row];
        cell.textLabel.text = [NSString stringWithFormat:@"%@:%@",[self.titleDic objectForKey:key],[self.infoDic objectForKey:key]];
        if ([key isEqualToString:@"DownloadUrl"]) {
            cell.textLabel.textColor = [UIColor blueColor];
        }
    }
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (self.islevelTable) {
        return;
    }
    
    UITableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
    NSLog(@"----%@",cell.textLabel.text);
    
    if (indexPath.row == 3) {
        UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"更新" message:@"立即更新" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
        [alert show];
    }
}

- (void)checkVersion
{
    self.titleDic = [[NSDictionary alloc]initWithObjectsAndKeys:@"描述",@"Description",@"链接",@"DownloadUrl",@"版本号",@"VersionCode",@"名称",@"VersionName", nil];
    
    NSString *api = [Global api:[NSString stringWithFormat:@"Metadata/Version"]];
    AFHTTPRequestOperationManager *request = [Global createRequestWithType:Xml];
    [request GET:api parameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject) {
        //NSLog(@"---url:%@ responseObject:%@",operation.request.URL.absoluteString,responseObject);
        self.infoDic = (NSDictionary *)responseObject;
        
        self.dataTable.delegate = self;
        self.dataTable.dataSource = self;
        if([self checkIfHasNewVersion])
        {
            self.keys = @[@"VersionName",@"VersionCode",@"Description",@"DownloadUrl"];
        }else{
            self.keys = @[@"VersionName",@"VersionCode",@"Description"];
        }
        [self.dataTable reloadData];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"---url:%@",operation.request.URL.absoluteString);
        [[[UIAlertView alloc] initWithTitle:@"提示" message:@"数据加载失败" delegate:nil cancelButtonTitle:@"确定" otherButtonTitles: nil] show];
    }];
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
