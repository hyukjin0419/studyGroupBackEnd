package com.studygroup.studygroupbackend.admin.entity;

/**
 * AdminPermission enum defines various administrative permissions
 * that can be granted to users with admin roles in the system.
 */
public enum AdminPermission {
    // Permission to view all member information
    VIEW_ALL_MEMBERS,
    
    // Permission to force delete studies/groups
    FORCE_DELETE_STUDY,
    
    // Permission to manage announcements (create, update, delete)
    MANAGE_ANNOUNCEMENTS,
    
    // Permission to manage user roles
    MANAGE_USER_ROLES,
    
    // Permission to view system logs
    VIEW_SYSTEM_LOGS
}
