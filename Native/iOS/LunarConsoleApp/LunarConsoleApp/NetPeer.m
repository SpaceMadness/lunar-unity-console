//
//  NetPeer.m
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2019 Alex Lementuev, SpaceMadness.
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//

#import <CocoaAsyncSocket/GCDAsyncSocket.h>

#import "NetPeer.h"

const static int kTagMessage = 1;

@interface NetPeer () <GCDAsyncSocketDelegate>
{
    GCDAsyncSocket * _socket;
    GCDAsyncSocket * _clientSocket;
    
    dispatch_queue_t _delegateQueue;
    dispatch_queue_t _socketQueue;
}

@end

@implementation NetPeer

- (instancetype)init
{
    self = [super init];
    if (self)
    {
        _delegateQueue = dispatch_queue_create("Delegate Queue", DISPATCH_QUEUE_SERIAL);
        _socketQueue = dispatch_queue_create("Socket Queue", DISPATCH_QUEUE_SERIAL);
        _socket = [[GCDAsyncSocket alloc] initWithDelegate:self
                                             delegateQueue:_delegateQueue
                                               socketQueue:_socketQueue];
    }
    return self;
}

- (void)startListeningOnPort:(uint16_t)port
{
    NSError *error = nil;
    [_socket acceptOnPort:port error:&error];
    
    if (error != nil)
    {
        NSLog(@"Unable to start host: %@", error);
        return;
    }
}

- (void)connectToHost:(NSString *)host port:(uint16_t)port
{
    NSError *error = nil;
    [_socket connectToHost:host onPort:port error:&error];
    
    if (error != nil)
    {
        NSLog(@"Unable to connect to host %@", error);
        return;
    }
}

- (void)sendMessage:(NetPeerMessage *)message
{
    [_clientSocket writeData:[message toJsonData] withTimeout:-1 tag:kTagMessage];
}

- (void)receiveSocketMessage:(GCDAsyncSocket *)socket
{
    [socket readDataWithTimeout:-1 tag:kTagMessage];
}

- (void)shutdown
{
    [_socket disconnect];
}

#pragma mark -
#pragma mark GCDAsyncSocketDelegate

- (void)socket:(GCDAsyncSocket *)sock didAcceptNewSocket:(GCDAsyncSocket *)newSocket
{
    NSLog(@"Client connected");
    _clientSocket = newSocket;
    
    if ([_delegate respondsToSelector:@selector(clientDidConnect)])
    {
        [_delegate clientDidConnect];
    }
    
    [self receiveSocketMessage:_clientSocket];
}

- (void)socket:(GCDAsyncSocket *)sock didConnectToHost:(NSString *)host port:(uint16_t)port
{
    NSLog(@"Connected to server %@:%d", host, port);
    _clientSocket = sock;
    
    [self receiveSocketMessage:sock];
}

- (void)socket:(GCDAsyncSocket *)sock didReadData:(NSData *)data withTag:(long)tag
{
    if (tag == kTagMessage)
    {
        NetPeerMessage *message = [[NetPeerMessage alloc] initWithData:data];
        if ([message.name isEqualToString:@"_ack_"])
        {
            if ([_delegate respondsToSelector:@selector(peerDidReceiveAck:)])
            {
                [_delegate peerDidReceiveAck:self];
            }
        }
        else
        {
            dispatch_async(dispatch_get_main_queue(), ^{
                if ([_delegate respondsToSelector:@selector(peer:didReceiveMessage:)])
                {
                    [_delegate peer:self didReceiveMessage:message];
                }
                
                NetPeerMessage *msg = [[NetPeerMessage alloc] initWithName:@"_ack_"];
                [self sendMessage:msg];
            });
        }
    }
    
    [self receiveSocketMessage:sock];
}

@end

@implementation NetPeerMessage
{
    NSMutableDictionary * _payload;
}

- (instancetype)initWithName:(NSString *)name
{
    self = [super init];
    if (self)
    {
        _payload = [NSMutableDictionary new];
        _payload[@"name"] = name;
    }
    return self;
}

- (instancetype)initWithData:(NSData *)data
{
    self = [super init];
    if (self)
    {
        NSError *error = nil;
        id object = [NSJSONSerialization JSONObjectWithData:data options:0 error:&error];
        
        if (error != nil)
        {
            self = nil;
            return nil;
        }
        
        if (![object isKindOfClass:[NSDictionary class]])
        {
            self = nil;
            return nil;
        }
        
        NSString *name = object[@"name"];
        if (name == nil)
        {
            self = nil;
            return nil;
        }
        
        _payload = [NSMutableDictionary dictionaryWithDictionary:object];
    }
    return self;
}

- (id)objectForKeyedSubscript:(NSString *)key
{
    return _payload[key];
}

- (void)setObject:(id)obj forKeyedSubscript:(NSString *)key
{
    _payload[key] = obj;
}

- (NSData *)toJsonData
{
    NSError *error = nil;
    NSData *data = [NSJSONSerialization dataWithJSONObject:_payload options:0 error:&error];
    
    if (error != nil)
    {
        NSLog(@"Can't create json data: %@", error);
        return nil;
    }
    
    return data;
}

- (NSString *)name
{
    return _payload[@"name"];
}

@end
