//
//  NetPeer.h
//  LunarConsoleApp
//
//  Created by Alex Lementuev on 1/28/17.
//  Copyright © 2017 Space Madness. All rights reserved.
//

#import <Foundation/Foundation.h>

@class NetPeer;

@interface NetPeerMessage : NSObject

@property (nonatomic, readonly) NSString *name;
@property (nonatomic, readonly) NSDictionary *payload;

- (instancetype)initWithName:(NSString *)name;
- (instancetype)initWithData:(NSData *)data;

- (id)objectForKeyedSubscript:(NSString *)key;
- (void)setObject:(id)obj forKeyedSubscript:(NSString *)key;

- (NSData *)toJsonData;

@end

@protocol NetPeerDelegate <NSObject>

@optional
- (void)clientDidConnect;
- (void)peer:(NetPeer *)peer didReceiveMessage:(NetPeerMessage *)message;
- (void)peerDidReceiveAck:(NetPeer *)peer;

@end

@interface NetPeer : NSObject

@property (nonatomic, weak) id<NetPeerDelegate> delegate;

- (void)startListeningOnPort:(uint16_t)port;
- (void)connectToHost:(NSString *)host port:(uint16_t)port;

- (void)sendMessage:(NetPeerMessage *)message;
- (void)shutdown;

@end
