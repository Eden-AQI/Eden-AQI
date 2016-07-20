//
//  TNBlockAlertController.h
//  AlertControllerTest
//
//  Created by Xiao on 10/8/15.
//  Copyright © 2015 Perkinelmer. All rights reserved.
//

#import <UIKit/UIKit.h>
typedef NS_ENUM(NSUInteger, OSPAlertStyle) {
    OSPAlertView = 0,
    OSPActionSheet,
};
@class NTNBlockAlertView;
@class NTNBlockActionSheet;

typedef void (^TNAlertViewBlock)(void);
#if __IPHONE_OS_VERSION_MIN_REQUIRED < __IPHONE_8_0
typedef void (^TNActionSheetBlock)( UIActionSheet * _Nullable actionSheet,NSInteger buttonIndex);
#endif

@interface TNBlockAlertController : NSObject


@property (nonatomic, strong, nonnull)UIAlertController *alertController;

#if __IPHONE_OS_VERSION_MIN_REQUIRED < __IPHONE_8_0
@property (nonatomic, strong, nullable)NTNBlockAlertView *alertView;
@property (nonatomic, strong, nullable)NTNBlockActionSheet *actionSheetView;
#endif

- (nullable instancetype)initWithTitle:(nullable NSString *)title message:(nullable NSString *)message delegate:(nullable id)delegate cancelButtonTitle:(nullable NSString *)cancelButtonTitle destructiveButtonTitle:(nullable NSString *)destructiveButtonTitle otherActions:(nullable NSArray*)actionArray AlertStyle:(OSPAlertStyle)viewStyle;

- (nullable TNAlertViewBlock)blockForOk;
- (void)setBlockForOk:(nullable TNAlertViewBlock)blockForOk;
- (nullable TNAlertViewBlock)blockForCancel;
- (void)setBlockForCancel:(nullable TNAlertViewBlock)blockForCancel;
- (void)setBlockForDestructive:(nullable TNAlertViewBlock)blockForDestructive;

- (void)setBlock:(nullable TNAlertViewBlock)block atButtonIndex:(NSInteger)buttonIndex;
//- (void)setActionSheetBlock:(nullable TNActionSheetBlock)block atButtonIndex:(NSInteger)buttonIndex;//ios_7.0

- (void)show;
- (void)showFromDismiss:(BOOL)flag;
//-------only for OSPActionSheet
- (void)showInView:(nonnull UIView *)view withDirection:(UIPopoverArrowDirection)direction;
- (void)showFromBarButtonItem:(nonnull UIBarButtonItem *)item animated:(BOOL)animated;
- (void)showFromRect:(CGRect)rect inView:(nonnull UIView *)view animated:(BOOL)animated;

@end

#if __IPHONE_OS_VERSION_MIN_REQUIRED < __IPHONE_8_0
@interface NTNBlockAlertView : UIAlertView

@property (nonatomic, strong, nullable) NSMutableDictionary *buttonBlocks;
- (void)show;

@end

@interface NTNBlockActionSheet : UIActionSheet

@property (copy, nonatomic, nullable) TNActionSheetBlock dismissionBlock;

- (NSInteger)addButtonWithTitle:(nullable NSString *)title action:(nullable TNActionSheetBlock)action;    // returns index of button. 0 based.

@property (nonatomic, strong, nullable) NSMutableDictionary *buttonBlocks;

@end
#endif