/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.server.net.response;

import brutes.net.NetworkWriter;

/**
 *
 * @author Thiktak
 */
public class Response {
    private NetworkWriter writer;
    
    public Response(NetworkWriter writer) {
        this.writer = writer;
    }
    
    public NetworkWriter getWriter() {
        return this.writer;
    }
}
