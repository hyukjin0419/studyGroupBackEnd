/**
 * 회원 관리 모듈
 * 회원 목록 조회, 추가, 수정 및 비밀번호 초기화 기능을 처리합니다.
 * 참고: 회원 상태 변경(액티브/인액티브) 기능은 백엔드에 구현되지 않았습니다.
 */

// 회원 관리 모듈 전역 상태
const memberModule = {
    members: [],
    
    // 모듈 초기화
    async initMembers() {
        // 회원 관리 UI 초기화
        this.initMembersUI();
        
        // 회원 목록 로드
        await this.loadMembers();
        
        // 이벤트 리스너 설정
        this.setupEventListeners();
    },
    
    // 회원 관리 UI 초기화
    initMembersUI() {
        const membersContent = document.getElementById('members-content');
        if (!membersContent) return;
        
        // 회원 관리 UI HTML
        membersContent.innerHTML = `
            <div class="card mb-4">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h5 class="mb-0">사용자 목록</h5>
                    <button type="button" class="btn btn-primary btn-sm" id="btn-add-member">
                        <i class="bi bi-plus-circle me-1"></i> 사용자 추가
                    </button>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>사용자명</th>
                                    <th>이메일</th>
                                    <th>가입일</th>
                                    <th>업데이트일</th>
                                    <th>관리</th>
                                </tr>
                            </thead>
                            <tbody id="members-table-body">
                                <tr>
                                    <td colspan="6" class="text-center">로딩 중...</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            
            <!-- 회원 추가/수정 모달 -->
            <div class="modal fade" id="memberModal" tabindex="-1" aria-labelledby="memberModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="memberModalLabel">사용자 추가</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <form id="member-form">
                                <input type="hidden" id="member-id">
                                <div class="mb-3">
                                    <label for="member-username" class="form-label">사용자명 *</label>
                                    <input type="text" class="form-control" id="member-username" required>
                                </div>
                                <div class="mb-3">
                                    <label for="member-email" class="form-label">이메일 *</label>
                                    <input type="email" class="form-control" id="member-email" required>
                                </div>
                                <div class="mb-3">
                                    <label for="member-password" class="form-label">비밀번호 *</label>
                                    <input type="password" class="form-control" id="member-password">
                                    <div class="form-text">수정 시 입력하지 않으면 기존 비밀번호가 유지됩니다.</div>
                                </div>
                                <div class="mb-3">
                                    <label for="member-role" class="form-label">역할 *</label>
                                    <select class="form-select" id="member-role" required>
                                        <option value="USER">일반 사용자</option>
                                        <option value="ADMIN">관리자</option>
                                    </select>
                                </div>
                                <div class="mb-3">
                                    <label for="member-status" class="form-label">상태 *</label>
                                    <select class="form-select" id="member-status" required>
                                        <option value="ACTIVE">활성</option>
                                        <option value="INACTIVE">비활성</option>
                                    </select>
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
                            <button type="button" class="btn btn-primary" id="btn-save-member">저장</button>
                        </div>
                    </div>
                </div>
            </div>
        `;
    },
    
    // 회원 목록 로드
    async loadMembers() {
        console.log('Loading members list...');
        try {
            const response = await axios.get(`${window.adminCore.API_BASE_URL}/users`);
            console.log('Members API response:', response.data);
            
            if (response.data.success) {
                this.members = response.data.data;
                console.log(`Loaded ${this.members.length} members`);
                this.renderMembersTable();
            }
        } catch (error) {
            console.error('회원 목록 로드 오류:', error);
            document.getElementById('members-table-body').innerHTML = `
                <tr>
                    <td colspan="6" class="text-center text-danger">회원 목록을 불러오는 중 오류가 발생했습니다.</td>
                </tr>
            `;
        }
    },
    
    // 회원 테이블 렌더링
    renderMembersTable() {
        console.log('Rendering members table...');
        const tableBody = document.getElementById('members-table-body');
        
        if (!tableBody) {
            console.error('members-table-body element not found');
            return;
        }
        
        if (this.members.length === 0) {
            tableBody.innerHTML = `
                <tr>
                    <td colspan="6" class="text-center">등록된 회원이 없습니다.</td>
                </tr>
            `;
            return;
        }
        
        let html = '';
        
        this.members.forEach(member => {
            // 날짜 형식화
            
            html += `
                <tr>
                    <td>${member.id}</td>
                    <td>${member.userName}</td>
                    <td>${member.email}</td>
                    <td>${this.formatDate(member.createdAt)}</td>
                    <td>${this.formatDate(member.updatedAt)}</td>
                    <td>
                        <div class="btn-group btn-group-sm" role="group">
                            <button class="btn btn-sm btn-primary edit-member" data-id="${member.id}" title="사용자 정보 수정">
                                <i class="bi bi-pencil-square"></i>
                            </button>
                            <button class="btn btn-sm btn-warning reset-password" data-id="${member.id}" title="비밀번호 초기화">
                                <i class="bi bi-key"></i>
                            </button>
                            <button class="btn btn-sm btn-danger delete-member" data-id="${member.id}" title="회원 삭제">
                                <i class="bi bi-trash"></i>
                            </button>
                        </div>
                    </td>
                </tr>
            `;
        });
        
        tableBody.innerHTML = html;
        console.log('Members table rendered with', this.members.length, 'rows');
        
        // 버튼 이벤트 리스너 추가
        this.addTableButtonListeners();
    },
    
    // 테이블 버튼 이벤트 리스너 추가
    addTableButtonListeners() {
        console.log('Adding table button listeners...');
        // 수정 버튼 이벤트 추가
        const editButtons = document.querySelectorAll('.edit-member');
        console.log(`Found ${editButtons.length} edit buttons`);
        
        editButtons.forEach(button => {
            button.addEventListener('click', () => {
                const memberId = button.getAttribute('data-id');
                console.log(`Edit button clicked for member ID: ${memberId}`);
                if (memberId) {
                    this.showEditMemberModal(memberId);
                }
            });
        });
        
        // 비밀번호 초기화 버튼 이벤트 추가
        const resetButtons = document.querySelectorAll('.reset-password');
        console.log(`Found ${resetButtons.length} password reset buttons`);
        
        resetButtons.forEach(button => {
            button.addEventListener('click', () => {
                const memberId = button.getAttribute('data-id');
                this.confirmResetPassword(memberId);
            });
        });
        
        // 삭제 버튼 이벤트 추가
        const deleteButtons = document.querySelectorAll('.delete-member');
        console.log(`Found ${deleteButtons.length} delete buttons`);
        
        deleteButtons.forEach(button => {
            button.addEventListener('click', () => {
                const memberId = button.getAttribute('data-id');
                this.confirmDeleteMember(memberId);
            });
        });
        
        // 상태 뱃지 표시를 위한 참조 코드 (클릭 이벤트 없음)
        const statusBadges = document.querySelectorAll('.status-badge');
        console.log(`Found ${statusBadges.length} status badges (non-clickable)`);
    },
    
    // 이벤트 리스너 설정
    setupEventListeners() {
        // 사용자 추가 버튼
        const addButton = document.getElementById('btn-add-member');
        if (addButton) {
            addButton.addEventListener('click', () => this.showAddMemberModal());
        }
        
        // 사용자 저장 버튼
        const saveButton = document.getElementById('btn-save-member');
        if (saveButton) {
            saveButton.addEventListener('click', () => this.saveMember());
        }
    },
    
    // 회원 추가 모달 표시
    showAddMemberModal() {
        // 모달 타이틀 설정
        document.getElementById('memberModalLabel').textContent = '사용자 추가';
        
        // 폼 초기화
        document.getElementById('member-form').reset();
        document.getElementById('member-id').value = '';
        
        // 모든 필드 활성화 및 표시
        const passwordField = document.getElementById('member-password');
        passwordField.disabled = false;
        passwordField.required = true;
        passwordField.value = '';
        
        // 비밀번호 필드 폼 그룹 표시
        const passwordFormGroup = passwordField.closest('.form-group') || passwordField.closest('.mb-3');
        if (passwordFormGroup) passwordFormGroup.style.display = '';
        
        // 역할 필드 표시
        const roleSelect = document.getElementById('member-role');
        if (roleSelect) {
            roleSelect.disabled = false;
            roleSelect.value = 'USER';
            const roleFormGroup = roleSelect.closest('.form-group') || roleSelect.closest('.mb-3');
            if (roleFormGroup) roleFormGroup.style.display = '';
        }
        
        // 상태 필드 제거 (백엔드에 구현되지 않음)
        const statusSelect = document.getElementById('member-status');
        if (statusSelect) {
            const statusFormGroup = statusSelect.closest('.form-group') || statusSelect.closest('.mb-3');
            if (statusFormGroup) statusFormGroup.style.display = 'none';
        }
        
        // 수정 모닜에서 추가한 안내 메시지 제거
        const infoDiv = document.getElementById('edit-mode-info');
        if (infoDiv) {
            infoDiv.remove();
        }
        
        // 안내 메시지 추가
        const addInfoDiv = document.createElement('div');
        addInfoDiv.id = 'add-mode-info';
        addInfoDiv.className = 'alert alert-info mt-2';
        addInfoDiv.innerHTML = '<strong>안내:</strong> 새 회원을 추가합니다. 모든 필드를 입력해주세요.';
        
        // 모달 보디에 안내 메시지 추가
        const formElement = document.getElementById('member-form');
        if (!document.getElementById('add-mode-info')) {
            formElement.prepend(addInfoDiv);
        }
        
        // 모달 표시
        const modal = new bootstrap.Modal(document.getElementById('memberModal'));
        modal.show();
    },
    
    // 회원 수정 모달 표시
    async showEditMemberModal(memberId) {
        console.log(`Showing edit modal for member ID: ${memberId}`);
        try {
            const member = this.members.find(m => m.id == memberId);
            
            if (!member) {
                console.error(`ID가 ${memberId}인 회원을 찾을 수 없습니다.`);
                window.adminCore.showModal('오류', `사용자 정보를 찾을 수 없습니다.`);
                return;
            }
            
            console.log('Member data for editing:', member);
            
            // 모달 타이틀 변경
            document.getElementById('memberModalLabel').textContent = '사용자 정보 수정';
            
            // 폼 필드 값 설정
            document.getElementById('member-id').value = member.id;
            document.getElementById('member-username').value = member.userName;
            document.getElementById('member-email').value = member.email;
            
            // 비밀번호, 역할, 상태 필드 숨기기
            const passwordField = document.getElementById('member-password');
            const passwordFormGroup = passwordField.closest('.form-group') || passwordField.closest('.mb-3');
            if (passwordFormGroup) passwordFormGroup.style.display = 'none';
            
            const roleSelect = document.getElementById('member-role');
            const roleFormGroup = roleSelect ? (roleSelect.closest('.form-group') || roleSelect.closest('.mb-3')) : null;
            if (roleFormGroup) roleFormGroup.style.display = 'none';
            
            const statusSelect = document.getElementById('member-status');
            const statusFormGroup = statusSelect ? (statusSelect.closest('.form-group') || statusSelect.closest('.mb-3')) : null;
            if (statusFormGroup) statusFormGroup.style.display = 'none';
            
            // 수정 모달에 안내 메시지 추가
            const infoDiv = document.getElementById('edit-mode-info') || document.createElement('div');
            infoDiv.id = 'edit-mode-info';
            infoDiv.className = 'alert alert-info mt-2';
            infoDiv.innerHTML = '<strong>안내:</strong> 회원 정보 수정 시 이름과 이메일만 변경 가능합니다. 비밀번호는 별도의 초기화 버튼을 이용해주세요. 역할 변경은 현재 지원되지 않습니다.';
            
            // 모달 보디에 안내 메시지 추가
            const formElement = document.getElementById('member-form');
            if (!document.getElementById('edit-mode-info')) {
                formElement.prepend(infoDiv);
            }
            
            // 모달 표시
            const modalElement = document.getElementById('memberModal');
            if (modalElement) {
                const modal = new bootstrap.Modal(modalElement);
                modal.show();
                console.log('Member edit modal displayed');
            } else {
                console.error('memberModal element not found');
            }
        } catch (error) {
            console.error('회원 정보 수정 모달 표시 오류:', error);
            window.adminCore.showModal('오류', `사용자 정보 수정 오류: ${error.message}`);
        }
    },
    
    // 회원 저장 (추가 또는 수정)
    async saveMember() {
        // 폼 유효성 검사
        const form = document.getElementById('member-form');
        if (!form.checkValidity()) {
            form.reportValidity();
            return;
        }
        
        // 폼 데이터 수집
        const memberId = document.getElementById('member-id').value;
        let memberData = {
            userName: document.getElementById('member-username').value,
            email: document.getElementById('member-email').value
        };
        
        // 새 회원 추가인 경우에만 추가 데이터 포함
        if (!memberId) {
            // 회원 추가에는 비밀번호와 역할 데이터 포함
            const password = document.getElementById('member-password').value;
            const role = document.getElementById('member-role').value;
            
            if (password) memberData.password = password;
            if (role) memberData.role = role;
        }
        
        try {
            let response;
            if (memberId) {
                // 회원 수정
                response = await axios.put(`${window.adminCore.API_BASE_URL}/users/${memberId}`, memberData);
                window.adminCore.showModal('알림', '회원 정보가 성공적으로 수정되었습니다.');
            } else {
                // 회원 추가
                response = await axios.post(`${window.adminCore.API_BASE_URL}/users`, memberData);
                window.adminCore.showModal('알림', '새 회원이 성공적으로 추가되었습니다.');
            }
            
            // 모달 닫기
            bootstrap.Modal.getInstance(document.getElementById('memberModal')).hide();
            
            // 회원 목록 갱신
            await this.loadMembers();
        } catch (error) {
            console.error('회원 저장 실패:', error);
            let errorMessage = '회원 정보 저장 중 오류가 발생했습니다.';
            
            if (error.response && error.response.data && error.response.data.message) {
                errorMessage = error.response.data.message;
            }
            
            window.adminCore.showModal('오류', errorMessage);
        }
    },
    
    // 사용자 상태 변경
    toggleUserStatus(memberId, currentStatus) {
        const member = this.members.find(m => m.id == memberId);
        if (!member) {
            console.error(`ID가 ${memberId}인 회원을 찾을 수 없습니다.`);
            return;
        }
        
        // 현재와 반대 상태
        const newStatus = currentStatus === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE';
        
        window.adminCore.showModal(
            '사용자 상태 변경 확인',
            `${member.userName} 사용자의 상태를 ${currentStatus} 에서 ${newStatus}(으)로 변경하시겠습니까?`,
            () => this.updateUserStatus(memberId, newStatus)
        );
    },
    
    // 사용자 상태 변경 실행
    async updateUserStatus(memberId, newStatus) {
        try {
            console.log(`Updating status for member ID: ${memberId} to ${newStatus}`);
            
            // API 호출
            const response = await axios.patch(`${window.adminCore.API_BASE_URL}/users/${memberId}/status`, {
                status: newStatus
            });
            
            if (response.data.success) {
                // 멤버 상태 업데이트
                const memberIndex = this.members.findIndex(m => m.id == memberId);
                if (memberIndex !== -1) {
                    this.members[memberIndex].status = newStatus;
                    this.renderMembersTable();
                }
                
                window.adminCore.showModal('상태 변경 완료', `사용자 상태가 ${newStatus}(으)로 성공적으로 변경되었습니다.`);
            } else {
                window.adminCore.showModal('오류', '사용자 상태 변경 중 오류가 발생했습니다.');
            }
        } catch (error) {
            console.error('사용자 상태 변경 오류:', error);
            
            // 기능이 아직 구현되지 않은 경우 메시지 표시
            if (error.response && error.response.status === 404) {
                window.adminCore.showModal(
                    '기능 미구현', 
                    `사용자 상태 변경 API가 아직 구현되지 않았습니다.<br><br>
                    허나 실제로 구현하려면 다음 API 엔드포인트를 추가해야 합니다:<br>
                    <code>PATCH /api/admin/users/{memberId}/status</code>`
                );
            } else {
                window.adminCore.showModal('오류', `사용자 상태 변경 중 오류가 발생했습니다: ${error.message}`);
            }
        }
    },
    
    // 회원 삭제 확인
    async confirmDeleteMember(memberId) {
        const member = this.members.find(m => m.id == memberId);
        if (!member) {
            console.error(`ID가 ${memberId}인 회원을 찾을 수 없습니다.`);
            return;
        }
        
        try {
            // 해당 회원이 리더인 스터디 목록 조회
            const response = await axios.get(`${window.adminCore.API_BASE_URL}/users/${memberId}/led-studies`);
            const ledStudies = response.data.data || [];
            
            let confirmMessage = `<strong class="text-danger">주의: 이 작업은 취소할 수 없습니다.</strong><br><br>${member.userName} 사용자를 영구적으로 삭제하시겠습니까?`;
            
            // 리더인 스터디가 있는 경우 경고 추가
            if (ledStudies.length > 0) {
                confirmMessage += `<br><br><strong class="text-danger">이 회원은 다음 스터디의 리더입니다:</strong><ul>`;
                
                ledStudies.forEach(study => {
                    confirmMessage += `<li>${study.name}</li>`;
                });
                
                confirmMessage += `</ul>회원 삭제 시 위 스터디도 모두 삭제됩니다!`;
            }
            
            confirmMessage += `<br><br>해당 사용자의 모든 데이터와 활동 기록이 삭제됩니다.`;
            
            window.adminCore.showModal(
                '회원 삭제 확인',
                confirmMessage,
                () => this.deleteMember(memberId)
            );
            
        } catch (error) {
            console.error('스터디 목록 조회 오류:', error);
            
            // 오류 발생 시에도 기본 삭제 확인 메시지 표시
            window.adminCore.showModal(
                '회원 삭제 확인',
                `<strong class="text-danger">주의: 이 작업은 취소 할 수 없습니다.</strong><br><br>${member.userName} 사용자를 영구적으로 삭제하시겠습니까?<br/>해당 사용자의 모든 데이터와 활동 기록이 삭제됩니다.`,
                () => this.deleteMember(memberId)
            );
        }
    },
    
    // 회원 삭제 실행
    async deleteMember(memberId) {
        try {
            console.log(`Deleting member ID: ${memberId}`);
            
            // API 호출
            const response = await axios.delete(`${window.adminCore.API_BASE_URL}/users/${memberId}`);
            
            if (response.data.success) {
                // 멤버 목록에서 제거
                this.members = this.members.filter(m => m.id != memberId);
                this.renderMembersTable();
                
                window.adminCore.showModal('삭제 완료', '회원이 성공적으로 삭제되었습니다.');
            } else {
                window.adminCore.showModal('오류', '회원 삭제 중 오류가 발생했습니다.');
            }
        } catch (error) {
            console.error('회원 삭제 오류:', error);
            let errorMessage = '회원 삭제 중 오류가 발생했습니다.';
            
            if (error.response && error.response.data && error.response.data.message) {
                errorMessage = error.response.data.message;
            }
            
            window.adminCore.showModal('오류', errorMessage);
        }
    },
    
    // 비밀번호 초기화 확인
    confirmResetPassword(memberId) {
        const member = this.members.find(m => m.id == memberId);
        if (!member) {
            console.error(`ID가 ${memberId}인 회원을 찾을 수 없습니다.`);
            return;
        }
        
        window.adminCore.showModal(
            '비밀번호 초기화 확인',
            `${member.userName} 사용자의 비밀번호를 초기화하시겠습니까?<br/>초기화 후 생성된 임시 비밀번호가 표시됩니다.`,
            () => this.resetPassword(memberId)
        );
    },
    
    // 비밀번호 초기화 실행
    async resetPassword(memberId) {
        try {
            console.log(`Resetting password for member ID: ${memberId}`);
            
            // 임시 비밀번호 생성 (8자리 랜덤 문자열)
            const tempPassword = Math.random().toString(36).slice(-8);
            
            // API 호출
            const response = await axios.post(`${window.adminCore.API_BASE_URL}/users/${memberId}/reset-password`, {
                tempPassword: tempPassword
            });
            
            if (response.data.success) {
                window.adminCore.showModal(
                    '비밀번호 초기화 완료',
                    `사용자의 비밀번호가 성공적으로 초기화되었습니다.<br><br>
                    <strong>임시 비밀번호:</strong> ${tempPassword}<br><br>
                    사용자에게 이 비밀번호를 전달하여 사용하도록 안내해 주세요.`
                );
            } else {
                window.adminCore.showModal('오류', '비밀번호 초기화 중 오류가 발생했습니다.');
            }
        } catch (error) {
            console.error('비밀번호 초기화 오류:', error);
            
            // 기능이 아직 구현되지 않은 경우 메시지 표시
            if (error.response && error.response.status === 404) {
                window.adminCore.showModal(
                    '기능 미구현', 
                    `비밀번호 초기화 API가 아직 구현되지 않았습니다.<br><br>
                    허나 실제로 구현하려면 다음 API 엔드포인트를 추가해야 합니다:<br>
                    <code>POST /api/admin/users/{memberId}/reset-password</code>`
                );
            } else {
                window.adminCore.showModal('오류', `비밀번호 초기화 중 오류가 발생했습니다: ${error.message}`);
            }
        }
    },
    
    // 날짜 포맷 변환
    formatDate(dateString) {
        if (!dateString) return '-';
        
        try {
            const date = new Date(dateString);
            
            // 유효한 날짜인지 확인
            if (isNaN(date.getTime())) {
                return '-';
            }
            
            // 날짜 포맷팅
            const year = date.getFullYear();
            const month = String(date.getMonth() + 1).padStart(2, '0');
            const day = String(date.getDate()).padStart(2, '0');
            const hours = String(date.getHours()).padStart(2, '0');
            const minutes = String(date.getMinutes()).padStart(2, '0');
            
            return `${year}-${month}-${day} ${hours}:${minutes}`;
        } catch (error) {
            console.error('날짜 포맷 오류:', error);
            return '-';
        }
    }
};

// 전역 객체에 모듈 등록
window.memberModule = memberModule;
